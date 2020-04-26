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

public class Main {

	static final String INDEX_DIR = "lucene_index/dir";
	// static final String CBOR_FILE = "D:/got.cbor";
	public static String file_book;
	private IndexSearcher is = null;
	private QueryParser qp = null;
	private boolean customScore = true;
	ArrayList<String> books = new ArrayList<String>();

	public void indexAllParas() throws CborException, IOException {

		Directory indexdir = FSDirectory.open((new File(INDEX_DIR)).toPath());
		IndexWriterConfig conf = new IndexWriterConfig(new StandardAnalyzer());
		conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		File file = new File(file_book);
		IndexWriter iw = new IndexWriter(indexdir, conf);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String st;
		int counter = 0;
		String sent = "";
		while ((st = br.readLine()) != null) {
			if (st.length() < 90) {
				sent = sent + st;
				Document paradoc = new Document();
				paradoc.add(new StringField("id", Integer.toString(counter), Field.Store.YES));
				paradoc.add(new TextField("parabody", sent, Field.Store.YES));
				iw.addDocument(paradoc);
				counter = counter + 1;
				sent = "";
			} else {
				sent = sent + " " + st;
			}

			// System.out.println(p.getParaId());
		}
		br.close();
		iw.close();

	}

	public SimilarityBase getCustomSimilarity(final int tfidf) {
		SimilarityBase mySimiliarity = new SimilarityBase() {

			@Override
			protected float score(BasicStats stats, float freq, float docLen) {
				float freqScore = 0;
				switch (tfidf) {
				case 1:
					freqScore = 1 + (float) Math.log(freq);
				case 2:
					freqScore = (1 + (float) Math.log(freq))
							* ((float) Math.log(stats.getNumberOfDocuments() / stats.getDocFreq()));
				case 3:
					if (freq > 0)
						freqScore = 1;
					else
						freqScore = 0;

				}
				return freqScore;
			}

			@Override
			public String toString() {
				return null;
			}
		};
		return mySimiliarity;
	}

	public void indexPara(IndexWriter iw, Data.Paragraph para) throws IOException {
		Document paradoc = new Document();
		paradoc.add(new StringField("id", para.getParaId(), Field.Store.YES));
		paradoc.add(new TextField("parabody", para.getTextOnly(), Field.Store.YES));
		iw.addDocument(paradoc);
	}

	public HashMap<String, List<String>> doSearch(String qstring, int n) throws IOException, ParseException {
		if (is == null) {
			is = new IndexSearcher(DirectoryReader.open(FSDirectory.open((new File(INDEX_DIR).toPath()))));
		}

		if (false) {
			SimilarityBase mySimiliarity = new SimilarityBase() {
				protected float score(BasicStats stats, float freq, float docLen) {
					return 1;
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
		HashMap<String, List<String>> map1 = new HashMap<String, List<String>>();

		Query q;
		TopDocs tds;
		ScoreDoc[] retDocs;
		List<String> list = new ArrayList<String>();

		// System.out.println("Query: " + qstring);
		q = qp.parse(qstring);
		tds = is.search(q, n);
		retDocs = tds.scoreDocs;
		Document d;
		// System.out.println(retDocs.length);
		for (int i = 0; i < retDocs.length; i++) {
			d = is.doc(retDocs[i].doc);
			// System.out.println("Doc " + i);
			// System.out.println("Score " + tds.scoreDocs[i].score);
			// System.out.println(d.getField("id").stringValue());
			// System.out.println(d.getField("parabody").stringValue() + "\n");
			if (map1.get(d.getField("id").stringValue()) == null) {
				List<String> list1 = new ArrayList<String>();
				list1.add(d.getField("parabody").stringValue());
				map1.put((Double.toString(tds.scoreDocs[i].score)), list1);

			} else {
				list = map1.get(d.getField("id").stringValue());
				list.add(d.getField("parabody").stringValue());
				map1.put((Double.toString(tds.scoreDocs[i].score)), list);
			}

		}

		return map1;
	}

	public void customerScore(boolean custom) throws IOException {
		customScore = custom;
	}

	public void ReadCSV() {

	}

	public static void main(String[] args) throws CborException, IOException, ParseException {
		FileMerge f=new FileMerge();
		f.do_file(args[3]);
		Main a = new Main();
		int counter = 0;
		file_book = "output.txt";
		a.indexAllParas();
		HashMap<String, List<String>> map1 = new HashMap<String, List<String>>();
		HashMap<String, List<String>> map2 = new HashMap<String, List<String>>();
		List<String> list1 = new ArrayList<String>();
		csvReader obj = new csvReader();
		HashMap<String, String> Gender_Map = new HashMap<String, String>();
		Gender_Map = obj.reader(args[1]);
		for (Entry<String, String> entry : Gender_Map.entrySet()) {

			String name = entry.getKey();
			map1 = a.doSearch(name + " Mr", 50);
			map2 = a.doSearch(name + " Mrs", 50);
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

				if (Integer.parseInt(entry.getValue()) == 0) {
					//System.out.println("given character " + name + " is male");
					counter = counter + 1;

				}
			} else {

				if (Integer.parseInt(entry.getValue()) == 1) {
					//System.out.println("given character " + name + " is female");
					counter = counter + 1;

				}
			}

		}
		System.out.println("Gender Predictor Accuracy :");
		System.out.println(((float)460-counter)/(float)460);
			character_predict c=new character_predict();
		c.predict("output.txt");

		Gender g=new Gender();
			g.do_funct(args[2],args[0],"train_gender.csv");
			g.do_funct(args[2],args[1],"test_gender.csv");
		Popularity p=new Popularity();
		p.do_funct(args[2],"test_gender.csv","test_pop.csv");
		p.do_funct(args[2],"train_gender.csv","train_pop.csv");
		csv_wr csv=new csv_wr();
		csv.do_funct();
		csv_wr1 csv1=new csv_wr1();
		csv1.do_funct();
        
		 allegiance_predictor alle_obj= new allegiance_predictor();
		 alle_obj.do_funct(args[1],args[0],args[2],args[4]);
		Gender_tfidf gend_obj= new Gender_tfidf();
		gend_obj.do_funct(1,args[0],args[1],args[2]);
		gend_obj.do_funct(2,args[0],args[1],args[2]);



		/*
		 * Rule_based r=new Rule_based(); System.out.println("Evaluating rule based");
		 * r.rule_based(file_book); character_predict c=new character_predict();
		 * c.predict(args[1]);
		 * 
		 * System.out.println("names.txt file is being created"); csv cs=new csv();
		 * cs.csv( args[0]); System.out.println("All run are created");
		 */

	}

}