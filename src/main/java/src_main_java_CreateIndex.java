
import java.awt.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

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
public class CreateIndex {

	static final String INDEX_DIR = "lucene_index/dir";
	static final String CBOR_FILE = "C:/Users/Sai Arvind/Downloads/got.cbor";

	private IndexSearcher is = null;
	private QueryParser qp = null;
	private boolean customScore = false;
	
	public HashMap<String, String> Gender_Map
										= new HashMap<String,String>();
	
	
	
	
	
	
	public void indexAllParas() throws CborException, IOException {
		Directory indexdir = FSDirectory.open((new File(INDEX_DIR)).toPath());
		IndexWriterConfig conf = new IndexWriterConfig(new StandardAnalyzer());
		conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		IndexWriter iw = new IndexWriter(indexdir, conf);
		PrintWriter  myWriter = new PrintWriter ("filename.txt");
		PrintWriter  myWriter1 = new PrintWriter ("names_car.txt");

		int counter=0;
		for (Data.Page p: DeserializeData.iterableAnnotations(new FileInputStream(new File(CBOR_FILE)))) {
			myWriter1.write(p.getPageName());
			//System.out.println(p);
			if(counter==100) {
				for(Data.Page.SectionPathParagraphs line: p.flatSectionPathsParagraphs()) {
					
					
						myWriter1.println(line.getParagraph().getTextOnly());
					
				}
				
				
					
			
				myWriter.println(p);}
				this.indexPara(iw, p);
			counter++;
		}
		myWriter.close();
		iw.close();	
		myWriter1.close();
		
	}

	public void indexPara(IndexWriter iw, Data.Page para) throws IOException {
		Document paradoc = new Document();
		paradoc.add(new StringField("paraid", para.getPageId(), Field.Store.YES));
		paradoc.add(new TextField("parabody", para.getPageName(), Field.Store.YES));
		//System.out.println(para.getChildSections());
		iw.addDocument(paradoc);
	}

	public void doSearch(String qstring, int n) throws IOException, ParseException {
		if (is == null) {
			is = new IndexSearcher(DirectoryReader.open(FSDirectory.open((new File(INDEX_DIR).toPath()))));
		}

		if (customScore) {
			SimilarityBase mySimiliarity = new SimilarityBase() {
				protected float score(BasicStats stats, float freq, float docLen) {
					return freq;
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

		Query q;
		TopDocs tds;
		ScoreDoc[] retDocs;

		System.out.println("Query: " + qstring);
		q = qp.parse(qstring);
		tds = is.search(q, n);
		retDocs = tds.scoreDocs;
		Document d;
		for (int i = 0; i < retDocs.length; i++) {
			d = is.doc(retDocs[i].doc);
			System.out.println("Doc " + i);
			System.out.println("Score " + tds.scoreDocs[i].score);
			System.out.println(d.getField("paraid").stringValue());
			System.out.println(d.getField("parabody").stringValue() + "\n");

		}
	}

	public void customerScore(boolean custom) throws IOException {
		customScore = custom;
	}

	public static void main(String[] args) throws CborException, IOException, ParseException {
		CreateIndex a = new CreateIndex();
		int topSearch = 10;

			a.indexAllParas();
			a.doSearch("stark", 10);

	}

}