
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import com.opencsv.CSVWriter;

public class gender_csv {
	public static HashMap<String, String> Gender_Map
	= new HashMap<String,String>();
	public static void main(String args[]) throws IOException {
		
		
		gender_csv obj= new gender_csv();
		obj.reader("");
		  System.out.println(Gender_Map.size());
		
		
	}
	
	
	public static HashMap<String, String> reader(String csvFile) throws IOException {
		
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
             Gender_Map.put(data[1], data[7]);
         } br.close();

         return Gender_Map;
	}
	
	
	public static void writer(HashMap<String, Double>  fi,String name,String path) throws IOException {
		gender_csv obj= new gender_csv();
		obj.reader(path);
		FileWriter outputfile = new FileWriter(name); 
        CSVWriter writer = new CSVWriter(outputfile); 
        String[] header = { "Name", "score", "gender" }; 
        writer.writeNext(header); 
		for (HashMap.Entry<String, String> entry : Gender_Map.entrySet()) {
				Double x=fi.get(entry.getKey());
				  String[] body = {entry.getKey(),x.toString(),entry.getValue() }; 
			        writer.writeNext(body); 
				
				
				
				
		}
		writer.close();
		Gender_Map.clear();
		
	}

}
