package assign1;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Arrays;
import java.util.List;



public class TFIDFCalculator {
       public double tf(List<String> doc, String term) {
        double result = 0;
        for (String word : doc) {
            if (term.equalsIgnoreCase(word))
                result++;
        }
        return result / doc.size();
    }

    public double idf(ArrayList<ArrayList<String>> documents, String term) {
        double n = 0;
        for (ArrayList<String> doc : documents) {
            for (String word : doc) {
                if (term.equalsIgnoreCase(word)) {
                    n++;
                    break;
                }
            }
        }
        return Math.log(documents.size() / n);
    }

   
	/*
	 * public double tfIdf(List<String> words, List<List<String>> documents, String
	 * term) { System.out.println(tf(words, term)); // return tf(words, term) *
	 * idf(documents, term);
	 * 
	 * }
	 */

    public static void main(String[] args) {

    	
    	
    	ArrayList<String> list= new ArrayList<String>();
    	list.add("Some wiki engines are open source, wiki whereas others are proprietary");
    	list.add("Some permit control over different functions wiki ");
    	
    	ArrayList<String> words = new ArrayList<String>();
    	 	
    	for(int i=0;i<list.size();i++) {
    	String[] s=	list.get(i).split(" ");
    	for(int j=0;j<s.length;j++) {
    		if(!words.contains(s[j]) && !(s[j]=="")) {
    			words.add(s[j]);
    		}
    	}
    	}
		/*
		 * for(int i=0;i<words.size();i++) { System.out.println(words.get(i));
		 * 
		 * }
		 */
    	
    	
    	
    	TFIDFCalculator obj= new TFIDFCalculator();
    	System.out.println(obj.n_tf(words,"wiki"));
    	
    	System.out.println("asd"+obj.n_idf(list, "wiki"));
    	
        List<String> doc1 = Arrays.asList("Lorem", "ipsum", "dolor", "ipsum", "sit", "ipsum");
        List<String> doc2 = Arrays.asList("Vituperata", "incorrupte", "at", "ipsum", "pro", "quo");
        List<String> doc3 = Arrays.asList("Has", "persius", "disputationi", "id", "simul");
        List<List<String>> documents = Arrays.asList(doc1, doc2, doc3);
        TFIDFCalculator calculator = new TFIDFCalculator();
      //  double tfidf = calculator.tfIdf(doc1, documents, "ipsum");
  //      System.out.println("TF-IDF (ipsum) = " + tfidf);


    }
public double n_tf(ArrayList<String> words , String term) {
	double result = 0;
    for (String word : words) {
        if (term.toLowerCase().contains(word.toLowerCase()))
            result++;
    }
    return result / words.size();

}
public double n_idf(ArrayList<String> documents, String term) {
    double n = 0;
    double size=0;
    for (String doc : documents) {
    		if(doc.contains(term)) {
    			n++;
    		}
    		if(n==0) {
    			return 0;
    		}
    	
            }
    return Math.log(documents.size() / n);
}




}