
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import com.opencsv.CSVWriter;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.SimilarityBase;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import co.nstant.in.cbor.CborException;
import edu.unh.cs.treccar_v2.Data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.SimilarityBase;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import co.nstant.in.cbor.CborException;
import edu.unh.cs.treccar_v2.Data;
import edu.unh.cs.treccar_v2.read_data.DeserializeData;

public class Gender {

	static final String INDEX_DIR = "lucene_index/dir";
	//static final String CBOR_FILE = "C:/Users/Sai Arvind/Downloads/got.cbor";

	private IndexSearcher is = null;
	private QueryParser qp = null;
	private boolean customScore = true;

	public static HashMap<String, ArrayList<String>> inlinks = new HashMap<String, ArrayList<String>>();

	public static HashMap<String, ArrayList<String>> outlinks = new HashMap<String, ArrayList<String>>();

	public void indexAllParas(String CBOR_FILE) throws CborException, IOException {
		System.out.println("in index");
		Directory indexdir = FSDirectory.open((new File(INDEX_DIR)).toPath());
		IndexWriterConfig conf = new IndexWriterConfig(new StandardAnalyzer());
		conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		IndexWriter iw = new IndexWriter(indexdir, conf);
		PrintWriter myWriter = new PrintWriter("filename.txt");
		int counter = 0;
		for (Data.Page page : DeserializeData.iterableAnnotations(new FileInputStream(new File(CBOR_FILE)))) {
			String[] y = page.getSkeleton().toString().split("paragraph=Paragraph");
			int f = 0;
			int len;

			for (int p = 1; p < y.length - 1; p++) {
				// System.out.println("_____________________________________________________++");
				String name = y[p].substring(9, 49);
				String[] results = StringUtils.substringsBetween(y[p], "text='", "'}");
				String[] pages = StringUtils.substringsBetween(y[p], "page='", "'");

				// System.out.println(y[p]);

				// System.out.println("p is :"+p);

				try {
					int xe = results.length;
				} catch (Exception e) {
					continue;
				}

				try {
					len = results.length;
					;
					if (len - pages.length > 1)
						continue;

				} catch (Exception e) {
					f = 1;
					len = 2;

				}

				String x = "";
				int i = 0;

				for (; i < len - 1; i++) {
					if (f == 1) {
						f = 0;
						x = x + results[i];
						continue;

					}
					x = x + results[i] + pages[i];

				}
				if (results.length == 1) {

					// System.out.println(x);
					continue;
				}
				x = x + results[i];
				counter = counter + 1;

				// System.out.println(x);

				Document paradoc = new Document();
				paradoc.add(new StringField("id", name, Field.Store.YES));
				paradoc.add(new TextField("parabody", x, Field.Store.YES));
				iw.addDocument(paradoc);

			}
		}
		// myWriter.close();
		iw.close();

	}

	public void indexPara(IndexWriter iw, Data.Paragraph para) throws IOException {

	}

	/*
	 * public HashMap<String, List<String>> doSearch(String qstring, int n) throws
	 * IOException, ParseException { if (is == null) { is = new
	 * IndexSearcher(DirectoryReader.open(FSDirectory.open((new
	 * File(INDEX_DIR).toPath())))); }
	 * 
	 * if (false) { SimilarityBase mySimiliarity = new SimilarityBase() {
	 * 
	 * @Override protected float score(BasicStats stats, float freq, float docLen) {
	 * float norm = 1;
	 * 
	 * long docFreq = stats.getDocFreq(); long numDocs =
	 * stats.getNumberOfDocuments(); double lnc = (1 +
	 * Math.log(freq))*1*(1/Math.sqrt(Math.pow((1 + Math.log(freq)), 2))+1);
	 * 
	 * double ltn = (1 + Math.log(docFreq))*Math.log(numDocs/(double) docFreq)*1;
	 * return (float) (lnc*ltn);
	 * 
	 * }
	 * 
	 * @Override public String toString() { // TODO Auto-generated method stub
	 * return null; }
	 * 
	 * 
	 * 
	 * }; is.setSimilarity(mySimiliarity); }
	 * 
	 * 
	 * The first arg of QueryParser constructor specifies which field of document to
	 * match with query, here we want to search in the para text, so we chose
	 * parabody.
	 * 
	 * 
	 * if (qp == null) { qp = new QueryParser("parabody", new StandardAnalyzer()); }
	 * HashMap<String, List<String>> map1 = new HashMap<String, List<String>>();
	 * List<String> list = new ArrayList<String>(); Query q; TopDocs tds; ScoreDoc[]
	 * retDocs;
	 * 
	 * System.out.println("Query: " + qstring); q = qp.parse(qstring); tds =
	 * is.search(q, n); retDocs = tds.scoreDocs; Document d;
	 * System.out.println(retDocs.length); for (int i = 0; i < retDocs.length; i++)
	 * { d = is.doc(retDocs[i].doc); if(map1.get(d.getField("id").stringValue()) ==
	 * null) { List<String> list1 = new ArrayList<String>();
	 * list1.add(d.getField("parabody").stringValue()); map1.put((Double.toString(
	 * tds.scoreDocs[i].score)), list1);
	 * 
	 * }else { list=map1.get(d.getField("id").stringValue());
	 * list.add(d.getField("parabody").stringValue()); map1.put((Double.toString(
	 * tds.scoreDocs[i].score)), list); }
	 * 
	 * }
	 * 
	 * return map1; }
	 */
	
	public HashMap<String, List<String>> doSearch(String qstring, int n) throws IOException, ParseException {
		if (is == null) {
			is = new IndexSearcher(DirectoryReader.open(FSDirectory.open((new File(INDEX_DIR).toPath()))));
		}

		if (true) {
			SimilarityBase mySimiliarity = new SimilarityBase() {
				 protected float score(BasicStats stats, float freq, float docLen) {
                    	float norm = 1;
                        long docFreq = stats.getDocFreq();
                        long numDocs = stats.getNumberOfDocuments();
                        double res = (0.5 + ((0.5 * freq)/ stats.getTotalTermFreq()) ) / Math.sqrt(docLen);
                        
                        //apc
                    double apc=  (0.5 + ((0.5 * freq)/ stats.getTotalTermFreq()) )* (Math.max(0,  Math.log(numDocs-docFreq/(double) docFreq)))/ Math.sqrt(docLen);
                        return (float) (apc*res);
                        
                        	  
				}

				@Override
				public String toString() {
					return null;
				}
			};
			is.setSimilarity(mySimiliarity);
		}

		/*
		 * The first arg of QueryParser constructor specifies which field of document to
		 * match with query, here we want to search in the para text, so we chose
		 * parabody.
		 * 
		 */
		if (qp == null) {
			qp = new QueryParser("parabody", new StandardAnalyzer());
		}
		HashMap<String, List<String>> map1 = 
				new HashMap<String, List<String>>(); 
		

		Query q;
		TopDocs tds;
		ScoreDoc[] retDocs;
		List<String> list = new ArrayList<String>(); 

		//System.out.println("Query: " + qstring);
		q = qp.parse(qstring);
		tds = is.search(q, n);
		retDocs = tds.scoreDocs;
		Document d;
		//System.out.println(retDocs.length);
		for (int i = 0; i < retDocs.length; i++) {
			d = is.doc(retDocs[i].doc);
			//System.out.println("Doc " + i);
			//System.out.println("Score " + tds.scoreDocs[i].score);
			//System.out.println(d.getField("id").stringValue());
			//System.out.println(d.getField("parabody").stringValue() + "\n");
			if(map1.get(d.getField("id").stringValue()) == null) {
				List<String> list1 = new ArrayList<String>(); 
		list1.add(d.getField("parabody").stringValue());
				map1.put((Double.toString( tds.scoreDocs[i].score)), list1);
				
			}else {
			list=map1.get(d.getField("id").stringValue());
			list.add(d.getField("parabody").stringValue());
			map1.put((Double.toString( tds.scoreDocs[i].score)), list);
			}
		
		}

		return map1;
	}

	
	
	public void customerScore(boolean custom) throws IOException {
		customScore = custom;
	}

	public void do_funct(String cbor,String deat,String file1) throws IOException, CborException, ParseException{





		Gender a = new Gender();
		int counter = 0;
		a.indexAllParas(cbor);

		HashMap<String, List<String>> map1 = new HashMap<String, List<String>>();
		HashMap<String, List<String>> map2 = new HashMap<String, List<String>>();
		List<String> list1 = new ArrayList<String>();
		csvReader obj = new csvReader();
		HashMap<String, String> Gender_Map = new HashMap<String, String>();
		HashMap<String, String> Gender_Map_predict = new HashMap<String, String>();
		Gender_Map = obj.reader(deat);
		
		 File file = new File(file1); 
		
		        // create FileWriter object with file as parameter 
		        FileWriter outputfile = new FileWriter(file); 
		  
		        // create CSVWriter object filewriter object as parameter 
		        CSVWriter writer = new CSVWriter(outputfile);
		        String[] wr= {"name","gender","popularity"};
		        writer.writeNext(wr);
		for (Entry<String, String> entry : Gender_Map.entrySet()) {

			String name = entry.getKey();
			map1 = a.doSearch(name + " his", 50);
			map2 = a.doSearch(name + " her", 50);
			Double score = 0.0;
			Double score1 = 0.0;
			for (Entry<String, List<String>> entry1 : map1.entrySet()) {
				/*
				 * System.out.println("Key = " + entry.getKey() + ", Value = " +
				 * entry.getValue());
				 */
				list1 = entry1.getValue();
				score = Double.parseDouble(entry1.getKey()) + score;
			}

			for (Entry<String, List<String>> entry1 : map2.entrySet()) {

				list1 = entry1.getValue();
				score1 = Double.parseDouble(entry1.getKey()) + score1;
			}

			score = score / 5;
			score1 = score1 / 5;
			if (score > score1) {
					Gender_Map_predict.put(entry.getKey(), "1");
					String[] nextLine = {entry.getKey(),"1"};
					writer.writeNext(nextLine);
					if (Integer.parseInt(entry.getValue()) == 0) {
				//	System.out.println("given character " + name + " is male");
					counter = counter + 1;

				}
			} else {
				String[] nextLine = {entry.getKey(),"0"};
				writer.writeNext(nextLine);
				Gender_Map_predict.put(entry.getKey(), "0");
				if (Integer.parseInt(entry.getValue()) == 1) {
					//System.out.println("given character " + name + " is female");
					counter = counter + 1;

				}
			}

		}
		
		System.out.println(counter);
		System.out.println(Gender_Map_predict.size());
		
		
		writer.close();
		




}

}
