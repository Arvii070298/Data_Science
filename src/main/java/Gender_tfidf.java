import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.SimilarityBase;
import org.apache.lucene.store.FSDirectory;

import co.nstant.in.cbor.CborException;

public class Gender_tfidf {

	
	
	
	
	
	
	
	public static void do_funct(int qwe,String traincsv, String testcsv ,String cbor) throws IOException, CborException, ParseException {
		String csvFile="";
		if(qwe==1) {
		csvFile = traincsv;}
		if(qwe==2) {
		csvFile = testcsv;}

		allegiance_csv alleg_obj = new allegiance_csv();
		HashMap<String, String> Map = new HashMap<String, String>();
		HashMap<String, Double> fi = new HashMap<String, Double>();

		ArrayList<HashMap<String, String>> map_list = new ArrayList<HashMap<String, String>>();
		HashMap<String, ArrayList<String>> page_para = new HashMap<String, ArrayList<String>>();
		Map = alleg_obj.reader(csvFile);
		map_list.add(Map);
		// Map.putAll(alleg_obj.reader(csvFile[1]));
		map_list.add(Map);
		linking link_obj = new linking();
		link_obj.indexAllParas(cbor);
		ArrayList<String> p_ids = new ArrayList<String>();

		Gender_tfidf gender_obj = new Gender_tfidf();

		page_para = link_obj.page_para;

		// HashSet<String> unionKeys = new HashSet<>(Map.keySet());

		int count = 0;
		Set<String> s = new HashSet<String>(Map.keySet());
		s.retainAll(page_para.keySet());
		for (String ele : s) {
			Map.remove(ele);
			ArrayList<String> lis = new ArrayList<String>();
			ArrayList<String> lines = new ArrayList<String>();
			count = 0;
			lis = page_para.get(ele);
			TFIDFCalculator tfidf_obj = new TFIDFCalculator();
			for (int i = 0; i < lis.size(); i++) {
				String para = lis.get(i);
				Pattern pattern = Pattern.compile(
						"[^.!?\\s][^.!?]*(?:[.!?](?!['\"]?\\s|$)[^.!?]*)*[.!?]?['\"]?(?=\\s|$)",
						Pattern.MULTILINE | Pattern.COMMENTS);
				Matcher matcher = pattern.matcher(para);
				while (matcher.find()) {
					lines.add(matcher.group());
					count++;
				}

			}

			ArrayList<String> words = new ArrayList<String>();

			for (int i = 0; i < lines.size(); i++) {
				String[] s1 = lines.get(i).split(" ");
				for (int j = 0; j < s1.length; j++) {
					if (!words.contains(s1[j]) && !(s1[j] == "")) {
						words.add(s1[j]);
					}
				}
			}

			fi.put(ele, (tfidf_obj.n_tf(words, ele) * tfidf_obj.n_idf(lines, ele)));
		}
		StringEqualityPercentCheckUsingJaroWinklerDistance g_obj = new StringEqualityPercentCheckUsingJaroWinklerDistance();
		stringSimilarity string_obj = new stringSimilarity();
		HashMap<String, String> name_ids = new HashMap<String, String>();
		HashMap<String, String> dup = new HashMap<String, String>();

		name_ids = link_obj.name_ids;
		int x = 0;
		for (Entry<String, String> outer : Map.entrySet()) {
			String[] s4 = outer.getKey().split(" ");
			link_obj.doSearch(s4[0], 20);
			p_ids = link_obj.para_ids;

			ArrayList<String> page_id = new ArrayList<String>();

			if (p_ids.size() != 0) {
				for (int i = 0; i < p_ids.size(); i++) {
					// System.out.println(name_ids.get(p_ids.get(i)));
					page_id.add(name_ids.get(p_ids.get(i)));

				}
			} else {
				if (s4.length > 1) {
					link_obj.doSearch(s4[1], 20);
					p_ids = link_obj.para_ids;
					for (int i = 0; i < p_ids.size(); i++) {
						// System.out.println(name_ids.get(p_ids.get(i)));
						page_id.add(name_ids.get(p_ids.get(i)));

					}
					if (page_id.size() == 0)
						continue;

				} else {
					continue;
				}
			}

			ArrayList<String> lis = new ArrayList<String>();
			ArrayList<String> lines = new ArrayList<String>();
			count = 0;
			lis = page_para.get(gender_obj.mostCommon(page_id));
			TFIDFCalculator tfidf_obj = new TFIDFCalculator();
			for (int i = 0; i < lis.size(); i++) {
				String para = lis.get(i);
				Pattern pattern = Pattern.compile(
						"[^.!?\\s][^.!?]*(?:[.!?](?!['\"]?\\s|$)[^.!?]*)*[.!?]?['\"]?(?=\\s|$)",
						Pattern.MULTILINE | Pattern.COMMENTS);
				Matcher matcher = pattern.matcher(para);
				while (matcher.find()) {
					lines.add(matcher.group());
					count++;
				}

			}

			ArrayList<String> words = new ArrayList<String>();

			for (int i = 0; i < lines.size(); i++) {
				String[] s1 = lines.get(i).split(" ");
				for (int j = 0; j < s1.length; j++) {
					if (!words.contains(s1[j]) && !(s1[j] == "")) {
						words.add(s1[j]);
					}
				}
			}
			dup.put(outer.getKey(), "");
			fi.put(outer.getKey(), (tfidf_obj.n_tf(words, gender_obj.mostCommon(page_id))
					* tfidf_obj.n_idf(lines, gender_obj.mostCommon(page_id))));

		}

		Map.keySet().removeAll(dup.keySet());

		count = 0;
		for (Entry<String, String> outer : Map.entrySet()) {
			count++;
			String key = "";
			int min = 100;
			int curr = 0;
			for (Entry<String, ArrayList<String>> inner : page_para.entrySet()) {

				curr = gender_obj.calculate(outer.getKey(), inner.getKey());
				if (curr < min) {
					key = inner.getKey();
					min = curr;

				}

			}


			ArrayList<String> lis = new ArrayList<String>();
			ArrayList<String> lines = new ArrayList<String>();
			lis = page_para.get(key);
			TFIDFCalculator tfidf_obj = new TFIDFCalculator();
			for (int i = 0; i < lis.size(); i++) {
				String para = lis.get(i);
				Pattern pattern = Pattern.compile(
						"[^.!?\\s][^.!?]*(?:[.!?](?!['\"]?\\s|$)[^.!?]*)*[.!?]?['\"]?(?=\\s|$)",
						Pattern.MULTILINE | Pattern.COMMENTS);
				Matcher matcher = pattern.matcher(para);
				while (matcher.find()) {
					lines.add(matcher.group());
				}

			}

			ArrayList<String> words = new ArrayList<String>();

			for (int i = 0; i < lines.size(); i++) {
				String[] s1 = lines.get(i).split(" ");
				for (int j = 0; j < s1.length; j++) {
					if (!words.contains(s1[j]) && !(s1[j] == "")) {
						words.add(s1[j]);
					}
				}
			}

			fi.put(outer.getKey(), (tfidf_obj.n_tf(words, key) * tfidf_obj.n_idf(lines, key)));
		}
		
		
		System.out.println(fi.size()); 
		gender_csv gend_obj= new gender_csv();
		if(qwe==1) {
		gend_obj.writer(fi,"gender_tf_train.csv",traincsv);
		}
		if(qwe==2) {
			gend_obj.writer(fi,"gender_tf_test.csv",testcsv);

		}

		/*
		 * unionKeys.removeAll(page_para.keySet());
		 * 
		 * System.out.println(unionKeys.size()); System.out.println(unionKeys);
		 * Iterator<String> it = unionKeys.iterator();
		 * System.out.println(page_para.size()); int count=0; while (it.hasNext()) {
		 * 
		 * String element = it.next(); System.out.println(element);
		 * 
		 * for (Entry<String, ArrayList<String>> entry : page_para.entrySet()) { if(
		 * entry.getKey().toLowerCase().contains(element.toLowerCase())) {
		 * System.out.println(count++); }
		 * 
		 * 
		 */
	}

	public static <T> T mostCommon(ArrayList<T> list) {
		HashMap<T, Integer> map = new HashMap<>();

		for (T t : list) {
			Integer val = map.get(t);
			map.put(t, val == null ? 1 : val + 1);
		}

		Entry<T, Integer> max = null;

		for (Entry<T, Integer> e : map.entrySet()) {
			if (max == null || e.getValue() > max.getValue())
				max = e;
		}

		return max.getKey();
	}

	static int calculate(String x, String y) {
		int[][] dp = new int[x.length() + 1][y.length() + 1];

		for (int i = 0; i <= x.length(); i++) {
			for (int j = 0; j <= y.length(); j++) {
				if (i == 0) {
					dp[i][j] = j;
				} else if (j == 0) {
					dp[i][j] = i;
				} else {
					dp[i][j] = min(dp[i - 1][j - 1] + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)),
							dp[i - 1][j] + 1, dp[i][j - 1] + 1);
				}
			}
		}

		return dp[x.length()][y.length()];
	}

	public static int costOfSubstitution(char a, char b) {
		return a == b ? 0 : 1;
	}

	public static int min(int... numbers) {
		return Arrays.stream(numbers).min().orElse(Integer.MAX_VALUE);
	}

	/*
	 * if(page_para.get(element).size()==0) { count++; }
	 * 
	 * System.out.println(count);
	 */

}
