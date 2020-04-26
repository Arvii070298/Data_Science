import com.opencsv.CSVWriter;
import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.simple.*;
import edu.unh.cs.treccar_v2.Data;
import edu.unh.cs.treccar_v2.read_data.DeserializeData;

import java.io.*;
import java.util.*;

public class count {
    public static HashMap<String, Integer> rel_counts
            = new HashMap<String,Integer>();
    public static ArrayList<String> names= new ArrayList<String>();

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
    public static int min(int... numbers) {
        return Arrays.stream(numbers).min().orElse(Integer.MAX_VALUE);
    }
    public static int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }
    public static ArrayList<String> reader(String csvFile) throws
            IOException {
        ArrayList<String> l= new ArrayList<String>();
        //  csvFile = "C:/Users/Sai Arvind/eclipse-workspace/assign1/deaths-test.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        int counter=0;
        br = new BufferedReader(new FileReader(csvFile));
        while ((line = br.readLine()) != null) {
            if(counter==0) {
                counter=counter+1;
                continue;
            }counter=counter+1;
            String[] data = line.split(cvsSplitBy);
            names.add(data[1]);
            l.add(data[1]);
        } br.close();
return l;

    }

    public static void do_funct(String csvtest,String csvtrain,String cbor) throws Exception {

        count obj = new count();
        obj.reader(csvtest);
       // System.out.println(names.size());
        obj.reader(csvtest);
      //  System.out.println(names.size());


        ArrayList<String> relations = new ArrayList<String>();
        String s = "Great-grandfather\n" +
                "Great-grandmother\n" +
                "Great-uncle\n" +
                "Grandfather\n" +
                "Grandmother\n" +
                "Great-aunt\n" +
                "Uncle\n" +
                "Aunt\n" +
                "Father\n" +
                "Mother\n" +
                "Uncle (Husband of Aunt)\n" +
                "Sister\n" +
                "Brother-in-law\n" +
                "Brother\n" +
                "Sister-in-law\n" +
                "Husband\n" +
                "Wife\n" +
                "Cousin\n" +
                "Cousin’s wife\n" +
                "Cousin\n" +
                "Cousin’s husband\n" +
                "Nephew\n" +
                "Niece\n" +
                "Son\n" +
                "Daughter-in-law\n" +
                "Daughter\n" +
                "Son-in-law\n" +
                "First cousin once removed\n" +
                "Grandson\n" +
                "Granddaughter";
        String[] rel = s.split("\n");
        List<String> wordList = Arrays.asList(rel);
       // System.out.println(wordList);
        // Create a CoreNLP document
        for (Data.Page p : DeserializeData.iterableAnnotations(new FileInputStream(new File(cbor)))) {

            for (String value : names) {
                if (p.getPageName().toLowerCase().contains(value.toLowerCase())) {
                    for (Data.Page.SectionPathParagraphs line : p.flatSectionPathsParagraphs()) {
                        Document doc = new Document(line.getParagraph().getTextOnly());


                        // Document doc = new Document("Eddard Stark was the older brother of Benjen, Lyanna and the younger brother of Brandon Stark. He is the father of Robb, Sansa, Arya, Bran, and Rickon by his wife, Catelyn Tully, and uncle of Jon Snow, who he raised as his bastard son. He was a dedicated husband and father, a loyal friend, and an honorable lord.");
                        int count = 0;
                        // Iterate over the sentences in the document
                        for (Sentence sent : doc.sentences()) {
                            // Iterate over the triples in the sentence
                            for (RelationTriple triple : sent.openieTriples()) {
                                String sentence = triple.subjectLemmaGloss() + " " + triple.relationLemmaGloss() + " " + triple.objectLemmaGloss();
                                for (String value1 : wordList) {
                                    if (sentence.toLowerCase().contains(value1.toLowerCase())) {
                                        count++;
                                    }
                                }
                            }


                        }
                        if(rel_counts.get(value)==null){
                        rel_counts.put(value,count);}else
                        {
                           int t= rel_counts.get(value) ;
                           rel_counts.put(value,t+count);
                        }
                        //System.out.println(value +"::::"+count);
                        break;
                    }
                }
            }
        }
        System.out.println(rel_counts.size());

        for (Map.Entry<String,Integer> entry : rel_counts.entrySet()) {
            names.remove(entry.getKey());

        }
        System.out.println(names.size());

        linking link_obj= new linking();
        link_obj.indexAllParas(cbor);
        String s1="";
        for(String val : wordList){
         s1=s1+" "+val;
        }
        for (String value : names){
            link_obj.doSearch(value + " s1",5);
           String f= linking.res;
            Document doc = new Document(f);
            int count=0;
            for (Sentence sent : doc.sentences()) {
                // Iterate over the triples in the sentence
                for (RelationTriple triple : sent.openieTriples()) {
                    String sentence = triple.subjectLemmaGloss() + " " + triple.relationLemmaGloss() + " " + triple.objectLemmaGloss();
                    for (String value1 : wordList) {
                        if (sentence.toLowerCase().contains(value1.toLowerCase())) {
                            count++;
                        }
                    }
                }


            }
            if(rel_counts.get(value)==null){
                rel_counts.put(value,count);}else
            {
                int t= rel_counts.get(value) ;
                rel_counts.put(value,t+count);
            }
            //System.out.println(value +"::::"+count);
            break;

        }
        System.out.println(rel_counts.size());

        for (Map.Entry<String,Integer> entry : rel_counts.entrySet()) {
            names.remove(entry.getKey());

        }
        System.out.println(names.size());
ArrayList<String> pages = new ArrayList<String>();
        pages=linking.pagen;
        HashMap<String, String> Map = new HashMap<String, String>();


        for(String value : names){
            String key = "";
            int min = 100;
            int curr=0;
            for(String page : pages) {

                curr = obj.calculate(value,page);
                if (curr < min) {
                    key = page;
                    min = curr;

                }


            }Map.put(value,key);

        }


//System.out.println(Map.size());
  HashMap<String,String> pn = new HashMap<String,String>();

        for (Map.Entry<String,String> entry : Map.entrySet()) {
           if(pn.get(entry.getKey())==null){
               rel_counts.put(entry.getKey(),0);
               continue;
            }
        String val=pn.get(entry.getKey());
            Document doc = new Document(val);
            int count=0;
            for (Sentence sent : doc.sentences()) {
                // Iterate over the triples in the sentence
                for (RelationTriple triple : sent.openieTriples()) {
                    String sentence = triple.subjectLemmaGloss() + " " + triple.relationLemmaGloss() + " " + triple.objectLemmaGloss();
                    for (String value1 : wordList) {
                        if (sentence.toLowerCase().contains(value1.toLowerCase())) {
                            count++;
                        }
                    }
                }


            }
            if(rel_counts.get(entry.getKey())==null){
                rel_counts.put(entry.getKey(),count);}else
            {
                int t= rel_counts.get(entry.getKey()) ;
                rel_counts.put(entry.getKey(),t+count);
            }
            //System.out.println(value +"::::"+count);




        }

int
count=0;

        for (java.util.Map.Entry<String, Integer> entry : rel_counts.entrySet()) {
            if(entry.getValue()==0){
                count++;
            }

        }


        ArrayList<String> lis= new ArrayList<String>();
        lis= obj.reader(csvtrain);
        FileWriter outputfile = new FileWriter("count_train.csv");
        CSVWriter writer = new CSVWriter(outputfile);
        String[] header = { "Name", "count" };
        writer.writeNext(header);

        for(String v: lis){
            if(rel_counts.get(v)==null){
                String[] body = {v,Integer.toString(0) };
                writer.writeNext(body);continue;
            }
            int x=rel_counts.get(v);
            String[] body = {v,Integer.toString(x) };
            writer.writeNext(body);

        }
        writer.close();

        ArrayList<String> lis1= new ArrayList<String>();
        lis= obj.reader(csvtest);
        FileWriter outputfile1 = new FileWriter("count_test.csv");
        CSVWriter writer1 = new CSVWriter(outputfile1);
        String[] header1 = { "Name", "count" };
        writer1.writeNext(header);

        for(String va: lis){
            if(rel_counts.get(va)==null){
                String[] body = {va,Integer.toString(0) };
                writer.writeNext(body);continue;
            }
            int x1=rel_counts.get(va);
            String[] body = {va,Integer.toString(x1) };
            writer1.writeNext(body);

        }
        writer1.close();



    }


}