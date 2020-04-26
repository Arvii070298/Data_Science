
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import assign1.allegiance_csv;
import assign1.CreateIndex;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.Array;
import edu.unh.cs.treccar_v2.Data;
import edu.unh.cs.treccar_v2.read_data.DeserializeData;

public class allegiance_predictor {

	
	
	
	
	public static LinkedHashMap<String, Integer> count(ArrayList<String> list) {
		
		LinkedHashMap<String, Integer> lhm=new LinkedHashMap<String, Integer>();

		for(String str1:list){
		    int flag=0;
		    for(Entry<String, Integer> entry:lhm.entrySet()){   

		        if(entry.getKey().equals(str1)){
		            flag=1;
		            break;
		        }}
		        if(flag==0){
		            lhm.put(str1, 1);
		        }


		}

		int maxCount = 1;
		int currCount = 1;
		for (int i=1;i<list.size();++i) {
		  if (list.get(i).equals(list.get(i-1))) {
		    ++currCount;
		    if(list.size()==i+1){
		        maxCount = Math.max(lhm.get(list.get(i)), currCount); 
		      lhm.put(list.get(i), maxCount);
		    }
		  } else {
		      maxCount = Math.max(lhm.get(list.get(i-1)), currCount); 
		      lhm.put(list.get(i-1), maxCount);
		    currCount = 1;
		  }

		}
			return  lhm;
}
	
	
	public static void do_funct(String csvtest, String csvtrain,String cbor) throws CborException, IOException, ParseException {
		String csvFile[] = new String[2];
		csvFile[0] = csvtrain;
		csvFile[1] = csvtest;

		HashMap<String, String> respect_houses = new HashMap<String, String>();
		respect_houses.put("north", "stark");
		respect_houses.put("vale of arryn", "arryn");
		respect_houses.put("stromlands", "baratheon");
		respect_houses.put("reach", "tyrell");
		respect_houses.put("iron islands", "grey joy");
		respect_houses.put("dorne", "martell");
		respect_houses.put("crownlands", "targaryen");
		respect_houses.put("westerlands", "lannister");
		respect_houses.put("riverlands", "tully");
		HashMap<String, String> alleg_predict = new HashMap<String, String>();
		HashMap<String, String> dup_Map = new HashMap<String, String>();
		HashMap<String, String> fis = new HashMap<String, String>();

		HashMap<String, String> Map = new HashMap<String, String>();
		HashMap<String, String> Map1 = new HashMap<String, String>();
		HashMap<String, ArrayList<String>> Map2 = new HashMap<String, ArrayList<String>>();

		HashMap<String, String> web_csv = new HashMap<String, String>();
		web_csv web = new web_csv();
		web_csv = web.read_csv("");
		ArrayList<HashMap<String, String>> map_list = new ArrayList<HashMap<String, String>>();

		allegiance_csv alleg_obj = new allegiance_csv();
		try {
			Map = alleg_obj.reader(csvFile[0]);
			map_list.add(Map);
			Map.putAll(alleg_obj.reader(csvFile[1]));
			dup_Map.putAll(Map);
			map_list.add(Map);
			

			ArrayList<String> name_list = allegiance_csv.house_names;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		int count = 0;
		ArrayList<Integer> size = new ArrayList<Integer>();
		ArrayList<String> strings = new ArrayList<String>();
		ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>();
		CreateIndex obj = new CreateIndex();
		try {
			obj.indexAllParas();
			count++;

			// count of house names
			for (HashMap.Entry<String, String> entry : Map.entrySet()) {
				strings = obj.doSearch(entry.getKey(), 30);
				for (int i = 0; i < strings.size(); i++) {

					String result = strings.get(i);
					result = result.toLowerCase();
					if (result.contains("house")) {
						String[] arr = result.split(" ");

						Map1.put(entry.getKey(), arr[1]);
						count++;
						alleg_predict.put(entry.getKey(), result);
						break;
					}
				}

			}
			size.add(count);

			Map.keySet().removeAll(alleg_predict.keySet());

		} catch (CborException | IOException e) {
			e.printStackTrace();
		}

		// cbor parse para content and taking nearby entities

		// webcsv jouse names,alleg nort
		// map1 names-alleg
		count = 0;
		ArrayList<String> web_csv_keyList = new ArrayList<String>(web_csv.keySet());
		ArrayList<String> Map1_keyList1 = new ArrayList<String>(alleg_predict.keySet());
		Map1_keyList1.remove("of");
		for (int i = 0; i < Map1_keyList1.size(); i++) {
			for (int j = 0; j < web_csv_keyList.size(); j++) {
				String[] s = Map1_keyList1.get(i).split(" ");
				if (s.length < 2) {
					s[0] = s[0].toLowerCase();
					if (web_csv_keyList.get(j).toLowerCase().contains(s[0])) {
						count++;
						/*
						 * System.out.println(web_csv_keyList.get(j));
						 * System.out.println(Map1_keyList1.get(i));
						 */
						alleg_predict.put(Map1_keyList1.get(i),
								respect_houses.get(web_csv.get(web_csv_keyList.get(j)).toLowerCase()));
						break;
					}
				} else {
					s[1] = s[1].toLowerCase();
					if (web_csv_keyList.get(j).toLowerCase().contains(s[1])) {
						count++;
						/*
						 * System.out.println(web_csv_keyList.get(j));
						 * System.out.println(Map1_keyList1.get(i));
						 */
						alleg_predict.put(Map1_keyList1.get(i),
								respect_houses.get(web_csv.get(web_csv_keyList.get(j)).toLowerCase()));
						break;
					}
				}
				
				
				 

			}
		}
				Map.keySet().removeAll(alleg_predict.keySet());
				
			
		linking link_obj = new linking();
		Main Main_obj = new Main();
		ArrayList<String> list = new ArrayList<String>();
		list = allegiance_predictor.index(cbor);
		try {
			count = 0;
			int c = 0;
			link_obj.indexAllParas(cbor);

			
			// Main_obj.indexAllParas();
			// indexing
			for (Entry<String, String> entry : Map.entrySet()) {
				ArrayList<String> list1 = new ArrayList<String>();

				ArrayList<String> s = new ArrayList<String>();
				s = link_obj.doSearch(entry.getKey(), 3);
				StringBuffer sb = new StringBuffer();

				for (String s1 : s) {
					sb.append(s1);
					sb.append(" ");
				}
				String str = sb.toString();
				String fi = allegiance_predictor.Stopwords(str);
				fi = fi.replace(":", "");
				fi = fi.replace(".", "");
				fi = fi.replace("'", "");
				String[] arr = fi.split(" ");
				for (int i = 0; i < arr.length; i++) {
					if (list.contains(arr[i])) {
						if (!list1.contains(arr[i]))
							list1.add(arr[i]);

					}
					if (list1.size() != 0)
						Map2.put(entry.getKey(), list1);
				}

			}
			
			
			
			
			
			int c1=0;int f=0;
			for (Entry<String, String> outer : Map.entrySet()) {
				f=0;
				for (Entry<String, String> inner : web_csv.entrySet()) {					//	house name ---------- alleg name
					String key=outer.getKey();
					
					String[] s= key.split(" ");
					for(int i=0;i<s.length;i++) {
						if(inner.getKey().toLowerCase().contains(s[i].toLowerCase())) {c1++;
							//System.out.println(  s[i]   +":::::::::::::::::::::::::::::::::::"+inner.getKey() +" :"+inner.getValue());f=1;
							if(respect_houses.get(inner.getValue().toLowerCase())==null) {
								alleg_predict.put(outer.getKey(),"unknown");
							}else {
						//	System.out.println(outer.getKey()+"+++++++++++++++++++++++++++++++++++++++"+"------------------------"+inner.getKey());
							alleg_predict.put(outer.getKey(),respect_houses.get(inner.getValue().toLowerCase()));

							break;
							}			}
						
						
					if(f==1)break;	
					}
					
					
				}
				
				
			}
			Map.keySet().removeAll(alleg_predict.keySet());
			Map2.keySet().removeAll(alleg_predict.keySet());

			
		
			HashMap<String, ArrayList<String>> res_map = new HashMap<String, ArrayList<String>>();

			ArrayList<String> lis = new ArrayList<String>();
			int q = 0;
			for (Entry<String, ArrayList<String>> entry : Map2.entrySet()) {
				count = 0;

				/*
				 * System.out.println(entry.getKey()); System.out.println(entry.getValue());
				 */
				lis = entry.getValue();
				///System.out.println(lis.size());
				if (lis.size() <= 1) {
					continue;
				}

				String key = null;
				;ArrayList<String> res_list = new ArrayList<String>();
				for (int i = 0; i < lis.size(); i++) {			

				///	System.out.println(lis.get(i));

					ArrayList<String> strings1 = new ArrayList<String>();

					if (lis.get(i).length() <= 1) {
						continue;
					}

					for (Entry<String, String> inner : dup_Map.entrySet()) {
						if (inner.getKey().toLowerCase().contains(lis.get(i).toLowerCase())) {
							q = 0;
							key = inner.getKey();
							break;
						}
					}

					if (q == 0) {
						strings1 = obj.doSearch(key, 30);
						q = 1;
					} else {
					//	System.out.println("lis val is"+lis.get(i));
					//	strings1 = obj.doSearch(lis.get(i), 30);
					}
					for (int j = 0; j < strings1.size(); j++) {

						String result = strings1.get(j);
						result = result.toLowerCase();
						if (result.contains("house")) {
							//System.out.println(entry.getKey() + " : " + entry.getValue() + " : " + result);
							result=result.replace("category:", "");
							result=result.replace("house ","");
							result=result.replace(" ", "");
							if(result=="") {
								break;
							}
							res_list.add(result);
							count++;
							break;

						}
					}

				}res_map.put(entry.getKey(), res_list); 

			}
			
			/*
			 * for (Entry<String, ArrayList<String>> entry : res_map.entrySet()) {
			 * System.out.println(entry.getKey()+";"); for(int
			 * i=0;i<entry.getValue().size();i++) {
			 * System.out.print(entry.getValue().get(i)+" : ");
			 * 
			 * }System.out.println(" ");
			 * 
			 * 
			 * }
			 */
			
			int co=0;
			
			  for (Entry<String, ArrayList<String>> entry : res_map.entrySet()) { int xq=0;
			  for (Entry<String, String> inner : web_csv.entrySet()) {
					LinkedHashMap<String, Integer> lhm=new LinkedHashMap<String, Integer>();

					lhm=	  allegiance_predictor.count(entry.getValue());
			  
			  

				for(Entry<String, Integer> in:lhm.entrySet()){
				   // System.out.println("Maximum Sequential occurrence of element- "+in.getKey()+" is- "+in.getValue());//display result
					
					if(inner.getKey().toLowerCase().contains(in.getKey().toLowerCase())) {
						
						alleg_predict.put(entry.getKey(), respect_houses.get(inner.getValue().toLowerCase()));
						
					}
					
					
				}
			  
			  }
			  
			  
			  }
			 
			Map.keySet().removeAll(alleg_predict.keySet());
			res_map.keySet().removeAll(alleg_predict.keySet());
				
				
				
			/*
			 * for (HashMap.Entry<String, String> entry : alleg_predict.entrySet()) {
			 * Syst	em.out.println(entry.getKey()+ " : "+entry.getValue()); }
			 */
				
				for (HashMap.Entry<String, String> entry : Map.entrySet()) {
					alleg_predict.put(entry.getKey(), "none");
				}
			  
			  System.out.println(alleg_predict.size());
				alleg_csv obj1= new alleg_csv();
				alleg_csv.writer(alleg_predict,csvtest,"Allegiance_train.csv");
				alleg_csv.writer(alleg_predict,csvtrain,"Allegiance_test.csv");

		} catch (CborException | IOException e) {
			e.printStackTrace();
		}

		/*
		 * count=0; System.out.println(results.size()); ArrayList<String> inner= new
		 * ArrayList<String>(); for(int j=0;j<results.size();j++) {
		 * inner=results.get(j); for(int i=0;i<inner.size();i++) {
		 * 
		 * String result=inner.get(i); result=result.toLowerCase();
		 * if(result.contains("house")) { System.out.println(result); count++; break; }
		 * }
		 * 
		 * } System.out.println(count);
		 */
	}
	
	
	
	public static String Stopwords(String sampleText) {
		String[] stopWord = { "a", "about", "above", "after", "again", "against", "ain", "all", "am", "an", "and",
				"any", "are", "aren", "aren't", "as", "at", "be", "because", "been", "before", "being", "below",
				"between", "both", "but", "by", "can", "couldn", "couldn't", "d", "did", "didn", "didn't", "do", "does",
				"doesn", "doesn't", "doing", "don", "don't", "down", "during", "each", "few", "for", "from", "further",
				"had", "hadn", "hadn't", "has", "hasn", "hasn't", "have", "haven", "haven't", "having", "he", "her",
				"here", "hers", "herself", "him", "himself", "his", "how", "i", "if", "in", "into", "is", "isn",
				"isn't", "it", "it's", "its", "itself", "just", "ll", "m", "ma", "me", "mightn", "mightn't", "more",
				"most", "mustn", "mustn't", "my", "myself", "needn", "needn't", "no", "nor", "not", "now", "o", "of",
				"off", "on", "once", "only", "or", "other", "our", "ours", "ourselves", "out", "over", "own", "re", "s",
				"same", "shan", "shan't", "she", "she's", "should", "should've", "shouldn", "shouldn't", "so", "some",
				"such", "t", "than", "that", "that'll", "the", "their", "theirs", "them", "themselves", "then", "there",
				"these", "they", "this", "those", "through", "to", "too", "under", "until", "up", "ve", "very", "was",
				"wasn", "wasn't", "we", "were", "weren", "weren't", "what", "when", "where", "which", "while", "who",
				"whom", "why", "will", "with", "won", "won't", "wouldn", "wouldn't", "y", "you", "you'd", "you'll",
				"you're", "you've", "your", "yours", "yourself", "yourselves", "could", "he'd", "he'll", "he's",
				"here's", "how's", "i'd", "i'll", "i'm", "i've", "let's", "ought", "she'd", "she'll", "that's",
				"there's", "they'd", "they'll", "they're", "they've", "we'd", "we'll", "we're", "we've", "what's",
				"when's", "where's", "who's", "why's", "would", "able", "abst", "accordance", "according",
				"accordingly", "across", "act", "actually", "added", "adj", "affected", "affecting", "affects",
				"afterwards", "ah", "almost", "alone", "along", "already", "also", "although", "always", "among",
				"amongst", "announce", "another", "anybody", "anyhow", "anymore", "anyone", "anything", "anyway",
				"anyways", "anywhere", "apparently", "approximately", "arent", "arise", "around", "aside", "ask",
				"asking", "auth", "available", "away", "awfully", "b", "back", "became", "become", "becomes",
				"becoming", "beforehand", "begin", "beginning", "beginnings", "begins", "behind", "believe", "beside",
				"besides", "beyond", "biol", "brief", "briefly", "c", "ca", "came", "cannot", "can't", "cause",
				"causes", "certain", "certainly", "co", "com", "come", "comes", "contain", "containing", "contains",
				"couldnt", "date", "different", "done", "downwards", "due", "e", "ed", "edu", "effect", "eg", "eight",
				"eighty", "either", "else", "elsewhere", "end", "ending", "enough", "especially", "et", "etc", "even",
				"ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "except", "f", "far", "ff",
				"fifth", "first", "five", "fix", "followed", "following", "follows", "former", "formerly", "forth",
				"found", "four", "furthermore", "g", "gave", "get", "gets", "getting", "give", "given", "gives",
				"giving", "go", "goes", "gone", "got", "gotten", "h", "happens", "hardly", "hed", "hence", "hereafter",
				"hereby", "herein", "heres", "hereupon", "hes", "hi", "hid", "hither", "home", "howbeit", "however",
				"hundred", "id", "ie", "im", "immediate", "immediately", "importance", "important", "inc", "indeed",
				"index", "information", "instead", "invention", "inward", "itd", "it'll", "j", "k", "keep", "keeps",
				"kept", "kg", "km", "know", "known", "knows", "l", "largely", "last", "lately", "later", "latter",
				"latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "line", "little", "'ll",
				"look", "looking", "looks", "ltd", "made", "mainly", "make", "makes", "many", "may", "maybe", "mean",
				"means", "meantime", "meanwhile", "merely", "mg", "might", "million", "miss", "ml", "moreover",
				"mostly", "mr", "mrs", "much", "mug", "must", "n", "na", "name", "namely", "nay", "nd", "near",
				"nearly", "necessarily", "necessary", "need", "needs", "neither", "never", "nevertheless", "new",
				"next", "nine", "ninety", "nobody", "non", "none", "nonetheless", "noone", "normally", "nos", "noted",
				"nothing", "nowhere", "obtain", "obtained", "obviously", "often", "oh", "ok", "okay", "old", "omitted",
				"one", "ones", "onto", "ord", "others", "otherwise", "outside", "overall", "owing", "p", "page",
				"pages", "part", "particular", "particularly", "past", "per", "perhaps", "placed", "please", "plus",
				"poorly", "possible", "possibly", "potentially", "pp", "predominantly", "present", "previously",
				"primarily", "probably", "promptly", "proud", "provides", "put", "q", "que", "quickly", "quite", "qv",
				"r", "ran", "rather", "rd", "readily", "really", "recent", "recently", "ref", "refs", "regarding",
				"regardless", "regards", "related", "relatively", "research", "respectively", "resulted", "resulting",
				"results", "right", "run", "said", "saw", "say", "saying", "says", "sec", "section", "see", "seeing",
				"seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sent", "seven", "several", "shall",
				"shed", "shes", "show", "showed", "shown", "showns", "shows", "significant", "significantly", "similar",
				"similarly", "since", "six", "slightly", "somebody", "somehow", "someone", "somethan", "something",
				"sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specifically", "specified",
				"specify", "specifying", "still", "stop", "strongly", "sub", "substantially", "successfully",
				"sufficiently", "suggest", "sup", "sure", "take", "taken", "taking", "tell", "tends", "th", "thank",
				"thanks", "thanx", "thats", "that've", "thence", "thereafter", "thereby", "thered", "therefore",
				"therein", "there'll", "thereof", "therere", "theres", "thereto", "thereupon", "there've", "theyd",
				"theyre", "think", "thou", "though", "thoughh", "thousand", "throug", "throughout", "thru", "thus",
				"til", "tip", "together", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "ts",
				"twice", "two", "u", "un", "unfortunately", "unless", "unlike", "unlikely", "unto", "upon", "ups", "us",
				"use", "used", "useful", "usefully", "usefulness", "uses", "using", "usually", "v", "value", "various",
				"'ve", "via", "viz", "vol", "vols", "vs", "w", "want", "wants", "wasnt", "way", "wed", "welcome",
				"went", "werent", "whatever", "what'll", "whats", "whence", "whenever", "whereafter", "whereas",
				"whereby", "wherein", "wheres", "whereupon", "wherever", "whether", "whim", "whither", "whod",
				"whoever", "whole", "who'll", "whomever", "whos", "whose", "widely", "willing", "wish", "within",
				"without", "wont", "words", "world", "wouldnt", "www", "x", "yes", "yet", "youd", "youre", "z", "zero",
				"a's", "ain't", "allow", "allows", "apart", "appear", "appreciate", "appropriate", "associated", "best",
				"better", "c'mon", "c's", "cant", "changes", "clearly", "concerning", "consequently", "consider",
				"considering", "corresponding", "course", "currently", "definitely", "described", "despite", "entirely",
				"exactly", "example", "going", "greetings", "hello", "help", "hopefully", "ignored", "inasmuch",
				"indicate", "indicated", "indicates", "inner", "insofar", "it'd", "keep", "keeps", "novel",
				"presumably", "reasonably", "second", "secondly", "sensible", "serious", "seriously", "sure", "t's",
				"third", "thorough", "thoroughly", "three", "well", "wonder", "a", "about", "above", "above", "across",
				"after", "afterwards", "again", "against", "all", "almost", "alone", "along", "already", "also",
				"although", "always", "am", "among", "amongst", "amoungst", "amount", "an", "and", "another", "any",
				"anyhow", "anyone", "anything", "anyway", "anywhere", "are", "around", "as", "at", "back", "be",
				"became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being",
				"below", "beside", "besides", "between", "beyond", "bill", "both", "bottom", "but", "by", "call", "can",
				"cannot", "cant", "co", "con", "could", "couldnt", "cry", "de", "describe", "detail", "do", "done",
				"down", "due", "during", "each", "eg", "eight", "either", "eleven", "else", "elsewhere", "empty",
				"enough", "etc", "even", "ever", "every", "everyone", "everything", "everywhere", "except", "few",
				"fifteen", "fify", "fill", "find", "fire", "first", "five", "for", "former", "formerly", "forty",
				"found", "four", "from", "front", "full", "further", "get", "give", "go", "had", "has", "hasnt", "have",
				"he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "him",
				"himself", "his", "how", "however", "hundred", "ie", "if", "in", "inc", "indeed", "interest", "into",
				"is", "it", "its", "itself", "keep", "last", "latter", "latterly", "least", "less", "ltd", "made",
				"many", "may", "me", "meanwhile", "might", "mill", "mine", "more", "moreover", "most", "mostly", "move",
				"much", "must", "my", "myself", "name", "namely", "neither", "never", "nevertheless", "next", "nine",
				"no", "nobody", "none", "noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on",
				"once", "one", "only", "onto", "or", "other", "others", "otherwise", "our", "ours", "ourselves", "out",
				"over", "own", "part", "per", "perhaps", "please", "put", "rather", "re", "same", "see", "seem",
				"seemed", "seeming", "seems", "serious", "several", "she", "should", "show", "side", "since", "sincere",
				"six", "sixty", "so", "some", "somehow", "someone", "something", "sometime", "sometimes", "somewhere",
				"still", "such", "system", "take", "ten", "than", "that", "the", "their", "them", "themselves", "then",
				"thence", "there", "thereafter", "thereby", "therefore", "therein", "thereupon", "these", "they",
				"thickv", "thin", "third", "this", "those", "though", "three", "through", "throughout", "thru", "thus",
				"to", "together", "too", "top", "toward", "towards", "twelve", "twenty", "two", "un", "under", "until",
				"up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever", "when", "whence",
				"whenever", "where", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether",
				"which", "while", "whither", "who", "whoever", "whole", "whom", "whose", "why", "will", "with",
				"within", "without", "would", "yet", "you", "your", "yours", "yourself", "yourselves", "the" };

		Set<String> stopWords = new HashSet<>(Arrays.asList(stopWord));

		StringBuffer clean = new StringBuffer();
		int index = 0;

		while (index < sampleText.length()) {
			// the only word delimiter supported is space, if you want other
			// delimiters you have to do a series of indexOf calls and see which
			// one gives the smallest index, or use regex
			int nextIndex = sampleText.indexOf(" ", index);
			if (nextIndex == -1) {
				nextIndex = sampleText.length() - 1;
			}
			String word = sampleText.substring(index, nextIndex);
			if (!stopWords.contains(word.toLowerCase())) {
				clean.append(word);
				if (nextIndex < sampleText.length()) {
					// this adds the word delimiter, e.g. the following space
					clean.append(sampleText.substring(nextIndex, nextIndex + 1));
				}
			}
			index = nextIndex + 1;
		}

		return clean.toString();
	}
	

	public static ArrayList<String> index(String CBOR_FILE) throws CborException, IOException {
		Directory indexdir = FSDirectory.open((new File("lucene_index/dir")).toPath());
		int counter = 0;
		ArrayList<String> li = new ArrayList<String>();

		for (Data.Page p : DeserializeData.iterableAnnotations(new FileInputStream(new File(CBOR_FILE)))) {
			li.add(p.getPageName());

		}
		return li;
	}

}
