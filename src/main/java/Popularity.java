import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
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

import com.opencsv.CSVWriter;

import co.nstant.in.cbor.CborException;
import edu.unh.cs.treccar_v2.Data;
import edu.unh.cs.treccar_v2.read_data.DeserializeData;
public class Popularity {

	static final String INDEX_DIR = "lucene_index/dir";
	//static final String CBOR_FILE = "C:/Users/Sai Arvind/Downloads/got.cbor";
	private static  ArrayList<String> inlink_node=new ArrayList<String>();
	private IndexSearcher is = null;
	private QueryParser qp = null;
	private boolean customScore = false;
	
	public static HashMap<String, ArrayList<String>> inlinks
										= new HashMap<String,ArrayList<String>>();
	
	public static HashMap<String, ArrayList<String>> outlinks
	= new HashMap<String,ArrayList<String>>();
	
	
	
	
	public void indexAllParas(String CBOR_FILE) throws CborException, IOException {
		Directory indexdir = FSDirectory.open((new File(INDEX_DIR)).toPath());
		IndexWriterConfig conf = new IndexWriterConfig(new StandardAnalyzer());
		conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
	//	IndexWriter iw = new IndexWriter(indexdir, conf);
		PrintWriter  myWriter = new PrintWriter ("filename.txt");
		int counter=0;
		for (Data.Page p: DeserializeData.iterableAnnotations(new FileInputStream(new File(CBOR_FILE)))) {
			ArrayList<String> list=new ArrayList<String>();
			//System.out.println(p);
			
					for(int i=0;i<p.getPageMetadata().getInlinkIds().size();i++) {
						String s=p.getPageMetadata().getInlinkIds().get(i);
							String[] strarr=s.split(":");
						s=	strarr[1].replaceAll("%20", " ");
							list.add(s);
					}
					inlinks.put(p.getPageName(), list);
					myWriter.println(p.getPageName());
					myWriter.println(list);
			counter++;
		}
		myWriter.close();
		//iw.close();	
		
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
	

	public void do_funct(String CBOR_FILE,String file_name,String predict_pop) throws IOException, CborException {

		Popularity a = new Popularity();

			a.indexAllParas(CBOR_FILE);
		
		  
		  for (Entry<String, ArrayList<String>>   outer: inlinks.entrySet()) {
			  ArrayList<String > list=new ArrayList<String>();
			  String key=outer.getKey();
			  for (Entry<String, ArrayList<String>>   inner: inlinks.entrySet()) {
				 if( inner.getValue().contains(key)) {
					 list.add(inner.getKey());
				 }
				 	
				  
				  
			  }
			//  if(list.size()==0)continue;
			  outlinks.put(key, list);
			  
		  
		  } ArrayList<String > list=new ArrayList<String>();
		  for (Entry<String, ArrayList<String>>   inner: outlinks.entrySet()) {
			  list.add(inner.getKey());
			  
			  
			  
		  }
		  
		  
		  float[][] twoD_arr = new float[outlinks.size()][15];
		  for(int i=0;i<outlinks.size();i++) {
			  twoD_arr[i][0]=(float)1/(float)outlinks.size();
		  }
		  System.out.println(twoD_arr.length);
		  
		
		  PrintWriter  myWriter1 = new PrintWriter ("ouput.txt");
		  for(int i=0;i<10;i++) {
			  
			  
			  for (Entry<String, ArrayList<String>>   inner: outlinks.entrySet()) {
				  float sum=0;
				  int pos=list.indexOf( inner.getKey());
				  String node=inner.getKey();
				  inlink_node=inlinks.get(node);
				  
				  for(int j=0;j<inlink_node.size();j++) {
					 if(list.indexOf(inlink_node.get(j))==-1)continue;
					  
					  
					sum=(sum+  twoD_arr[list.indexOf(inlink_node.get(j))][i])*(1/(float)outlinks.get(inlink_node.get(j)).size()) ;
				  }
				  	sum=sum*10000;
				  twoD_arr[pos][i+1]=sum;
				  myWriter1.println(inner.getKey()+"  "+sum);
				  
			  }
		  }
		  
		  
		  String csvFile = file_name;
	        BufferedReader br = null;
	        String line = "";
	        String cvsSplitBy = ",";

	        File file = new File(predict_pop); 
	        FileWriter outputfile = new FileWriter(file); 
	        CSVWriter writer = new CSVWriter(outputfile);
	        String[] wr= {"name","gender","popularity"};
	        writer.writeNext(wr);
	        
	        
	        
	        int count=0;int fl=1;
	        int c=0;
	            br = new BufferedReader(new FileReader(csvFile));
	            while ((line = br.readLine()) != null) {
	            		if(c==0){
	            			c=1;
	            			continue;
	            		}
	                // use comma as separator
	                String[] csv = line.split(cvsSplitBy);
	                csv[0]=csv[0].substring(1, (csv[0].length()-1));
	                	if(list.indexOf(csv[0])==-1) {
	                		for(int y=0;y<list.size();y++) {
	                			if(csv[0].contains(list.get(y))){
	                			String x="0";
	                				String[] nextLine= {csv[0],csv[1].substring(1, (csv[1].length()-1)),x};
	                				writer.writeNext(nextLine);
	                				fl=0;
	                				break;
	                			}
	                				
	                			
	                		}
	                		if(fl==1) {
		                		count=count+1;
		                	}fl=1;
	                	
	                	}else {
	                		String x="1";
	                		String[] nextLine= {csv[0],csv[1].substring(1, (csv[1].length()-1)),   x};
            				writer.writeNext(nextLine);
	                		
	                		
	                	}
	                	
	                	
	                	
	                	
	                	}
		  
	            System.out.println("count is "+count);
		  System.out.println(outlinks.size());
		  myWriter1.close();
		  writer.close();
		  br.close();
		  
		  
		  
		  

		
	}

}
