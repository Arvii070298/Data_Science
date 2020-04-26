
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import co.nstant.in.cbor.model.Map;

public class csvReader {
	public static HashMap<String, String> reader(String csvFile) throws IOException {
		HashMap<String, String> Gender_Map
											= new HashMap<String,String>();
		//csvFile = "C:/Users/Sai Arvind/eclipse-workspace/assign1/deaths-train.csv";
		BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        int counter=0;
		 br = new BufferedReader(new FileReader(csvFile));
         while ((line = br.readLine()) != null) {
        	 	if(counter==0) {
        	 			counter=counter+1;
        	 		continue;
        	 	}
             // use comma as separator
             String[] data = line.split(cvsSplitBy);
             Gender_Map.put(data[1], data[7]);
           
           //  freq_Finder(Gender_Map);
         } br.close();

         return Gender_Map;
	}
	
	public static HashMap<String, String> freq_Finder(HashMap<String, String> Gender_Map) throws IOException
	{
	 ArrayList<String> n= new ArrayList<String>();
	 HashMap<String, String> dup
		= new HashMap<String,String>();
		for (Entry<String, String> entry : Gender_Map.entrySet()) { 
				String name=entry.getKey();
					int count=0;
				String names[]=	name.split(" ");
				if(names.length>1) {
					int min=0;
					String pop_name="";
					for(int i=0;i<names.length;i++) {
						File file = new File("output.txt"); 
						  BufferedReader br = new BufferedReader(new FileReader(file)); 
						  String st;  
						while ((st = br.readLine()) != null) 
						{	    
						String temp[] = st.split(" ");
					      for (int j = 0; j < temp.length; j++) {
					         if (names[i].equals(temp[j])) 
					            count++;
					      }
						
						}
					
							if(count>min) {
								min=count;
								pop_name=names[i];
								
							}
					
					
					
					
					}
				String gender=	entry.getValue();
				System.out.println(entry.getKey()+"-----------"+pop_name);
					n.add(entry.getKey());
				
					//Gender_Map.put(pop_name, gender);
				dup.put(pop_name, gender)	;
				continue;
				}
				dup.put(entry.getKey(),entry.getValue());
			
		}
		
		
		
		
		
		return dup;
	}
	
	
	public static void main(String args[]) throws IOException {
			
       
        

    }

}