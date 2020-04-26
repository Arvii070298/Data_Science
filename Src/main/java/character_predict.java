
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import org.apache.commons.lang3.ArrayUtils;
public class character_predict {
	
	public static void predict(String file_textbook) throws IOException{
		File file = new File(file_textbook); 
		 String[] stopwords = {"a", "as", "able", "about", "above", "according","game","book","song","ice", "accordingly", "across", "actually", "after", "afterwards", "again", "against", "aint", "all", "allow", 
				"allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything",
				"anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "arent", "around", "as", "aside", "ask", "asking", "associated", "at", "available", "away",
				"awfully", "be", "became", "because", "become", "becomes", "becoming", "been","blood", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better",
				"between", "beyond", "both", "brief", "but", "by", "cmon", "cs", "came", "can", "cant", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com",
				"come", "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldnt", "course", "currently", "definitely",
				"described", "despite", "did", "didnt", "different", "do", "does", "doesnt", "doing", "dont", "done", "down", "downwards", "during", "each", "edu", "eg", "eight", "either", "else", "elsewhere",
				"enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "far", "few", "ff", "fifth",
				"first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", 
				"going", "gone", "got", "gotten", "greetings", "had", "hadnt", "happens", "hardly", "has", "hasnt", "have", "havent", "having", "he", "hes", "hello", "help", "hence", "her", "here", "heres", "hereafter", 
				"hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i", "id", "ill", "im", "ive", "ie", "if", "ignored", "immediate", 
				"in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is", "isnt", "it", "itd", "itll", "its", "its", "itself", "just", "keep",
				"keeps", "kept", "know", "knows", "known", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "little", "look", "looking", "looks", 
				"ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly",
				"necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", 
				"obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", 
				"outside", "over", "overall", "own","page", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "que", "quite", "qv", 
				"rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively", "respectively", "right", "said", "same", "saw", "say", "saying", "says", "second", "secondly", 
				"see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she", "should", "shouldnt", "since", 
				"six", "so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", 
				"such", "sup", "sure", "ts", "take", "taken", "tell", "tends", "th", "than","house", "thank", "thanks", "thanx", "that", "thats", "thats", "the", "their", "theirs", "them", "themselves", "then",
				"thence", "there", "theres", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "theyd", "theyll", "theyre", "theyve", "think", "third", "this", "thorough",
				"thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", 
				"un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "value", "various", "very", "via", "viz", "vs", "want", 
				"wants", "was", "wasnt", "way", "we", "wed", "well", "were", "weve", "welcome", "well", "went", "were", "werent", "what", "whats", "whatever", "when", "whence", "whenever", "where", "wheres", "whereafter",
				"whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whos", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without", 
				"wont", "wonder", "would", "would", "wouldnt", "yes", "yet", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves", "zero"};

        List<String> names = new ArrayList<String>(); 

		  BufferedReader br = new BufferedReader(new FileReader(file)); 
		  
		  String st; 
		  try {
			while ((st = br.readLine()) != null) {
			 //  System.out.println(st);
			    String[] words = st.split("\\s+");
			    String prev_word="";
			    for (int i = 0; i < words.length; i++) {
			        // You may want to check for a non-word character before blindly
			        // performing a replacement
			        // It may also be necessary to adjust the character class
			        words[i] = words[i].replaceAll("[^\\w]", "");
			      int counter=0;
			        if((!words[i].equals("")) && Character.isUpperCase(words[i].charAt(0) )&& (!ArrayUtils.contains(stopwords,words[i].toLowerCase()))  ) {
			        	int index=st.indexOf(words[i]);
			        	
			        	if(index<3 ||	Character.toString((st.charAt(index-2))).equals(".")) {
			        		//System.out.println(words[i]);
			        	}
			        	counter++;
			        //	names.add(words[i]);
			        	if(prev_word.contentEquals("")) {
			        		prev_word=words[i];
			        	}else {
			        	prev_word=prev_word+"_"+words[i];}
			        }else {
			        	counter=0;
			        }
			        if(counter==0 && (!prev_word.contentEquals(""))) {
			        	
			        	names.add(prev_word);
			        	prev_word="";
			        }
			    }
			//    System.out.println(" ");
			    
			  
			    
			    
			    
			}
		} catch (IOException e) {
			System.out.println("file read error");
			e.printStackTrace();
		} 
		  
	        List<String> list = new ArrayList<String>(); 
	        
	        for(int i=0;i<names.size();i++) {
	        	if(i==0) {
	        		list.add(names.get(i));
	        	}
	        	if((!list.contains(names.get(i)) && (list.indexOf(names.get(i))==-1)))
	        	{
	        		list.add(names.get(i));
	        	}
	        	
	        }
		  
		  
		  
		  
		  try {
		      File myObj = new File("names.txt");
		      if (myObj.createNewFile()) {
		    	  FileWriter fileWriter = new FileWriter("names.txt");
				    PrintWriter printWriter = new PrintWriter(fileWriter);
				    for(int i=0;i<list.size();i++) {
				    	printWriter.println(list.get(i));
				    }
				    printWriter.close();

		        System.out.println("name file is being created");
		      } else {
		        System.out.println("File already exists.");
		      }
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		
		
	}

}
