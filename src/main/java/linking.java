
import java.awt.List;
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
public class linking {

    static final String INDEX_DIR = "lucene_index/dir";
//	static final String CBOR_FILE = "C:/Users/Sai Arvind/Downloads/got.cbor";
    public static String name= "";
    static IndexSearcher is = null;
    static QueryParser qp = null;
    private boolean customScore = false;
public static String res="";
    public static HashMap<String, ArrayList<String>> page_names
            = new HashMap<String,ArrayList<String>>();
    public static HashMap<String, String> para_cont = new HashMap<String,String>();

    public static HashMap<String, ArrayList<String>> page_para
            = new HashMap<String,ArrayList<String>>();
    public static HashMap<String, String> page_name
            = new HashMap<String,String>();
    public static HashMap<String, String> paraid_name
            = new HashMap<String,String>();

    public static ArrayList<String> ids=new ArrayList<String>();
    public static HashMap<String,Integer> results = new HashMap<String,Integer>();
    public static HashMap<String,String> pname_1para = new HashMap<String,String>();

    public static HashMap<String,String> name_ids = new HashMap<String,String>();
    public static	ArrayList<String> para_ids=new ArrayList<String>();
    public static ArrayList<String> pagen=new ArrayList<String>();

    public static 	ArrayList<String> para_content=new ArrayList<String>();



    public void indexAllParas(String CBOR_FILE) throws CborException, IOException {
        Directory indexdir = FSDirectory.open((new File(INDEX_DIR)).toPath());
        IndexWriterConfig conf = new IndexWriterConfig(new StandardAnalyzer());
        conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter iw = new IndexWriter(indexdir, conf);
        int counter=0;



        for (Data.Page p: DeserializeData.iterableAnnotations(new FileInputStream(new File(CBOR_FILE)))) {
            int c=0;
            //System.out.println(p);
            ArrayList<String> list=new ArrayList<String>();
            ArrayList<String> list_para=new ArrayList<String>();

            for(Data.Page.SectionPathParagraphs line: p.flatSectionPathsParagraphs()) {
                    if(c==0){
                        pname_1para.put(p.getPageName(),line.getParagraph().getTextOnly());

                        c=1;
                    }
                Document paradoc = new Document();

                paradoc.add(new StringField("id", line.getParagraph().getParaId(), Field.Store.YES));
                paraid_name.put(line.getParagraph().getParaId(),p.getPageName());
                paradoc.add(new  TextField("parabody", line.getParagraph().getTextOnly(), Field.Store.YES));
                para_cont.put(line.getParagraph().getParaId(), line.getParagraph().getTextOnly());
                iw.addDocument(paradoc);
                list.add(line.getParagraph().getParaId());
                list_para.add(line.getParagraph().getTextOnly());
                name_ids.put(line.getParagraph().getParaId(), p.getPageName());
            }
                pagen.add(p.getPageName());
            page_names.put(p.getPageName(), list);
            page_para.put(p.getPageName(), list_para);

        }
        iw.close();

    }



    public ArrayList<String> doSearch(String qstring, int n) throws IOException, ParseException {
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
        ArrayList<String> p_id= new ArrayList<String>();
        Query q;
        TopDocs tds;
        ScoreDoc[] retDocs;
        ArrayList<String> list= new ArrayList<String>();
        q = qp.parse(qstring);
        tds = is.search(q, n);
        retDocs = tds.scoreDocs;
        Document d;
            res="";
        for (int i = 0; i < retDocs.length; i++) {
            d = is.doc(retDocs[i].doc);
            results.put(d.getField("id").stringValue(), 0);
            p_id.add(d.getField("id").stringValue());
            list.add(d.getField("parabody").stringValue());
            res=res+". "+d.getField("parabody").stringValue();
           // System.out.println("o/p is : ::::::::::::::::::::::::::"+d.getField("parabody").stringValue());
           //System.out.println( paraid_name.get(d.getField("id").stringValue()));
            name=paraid_name.get(d.getField("id").stringValue());
        }
        this.para_ids= p_id;
        return list;
    }

    public void customerScore(boolean custom) throws IOException {
        customScore = custom;
    }


 
    }