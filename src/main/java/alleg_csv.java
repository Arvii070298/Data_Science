
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import com.opencsv.CSVWriter;

public class alleg_csv {
	public static HashMap<String, String> Gender_Map = new HashMap<String, String>();
	
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
             Gender_Map.put(data[1], data[2]);
         } br.close();

         return Gender_Map;
	}
	
	
	public static void main(String args[]) throws IOException {
		
		
		
	}
	
	
	public static void writer(HashMap<String, String> map,String path,String name) throws IOException {
		

		alleg_csv obj= new alleg_csv();
		obj.reader(path);
		FileWriter outputfile = new FileWriter(name); 
        CSVWriter writer = new CSVWriter(outputfile); 
        String[] header = { "Name", "Allegiance" }; 
        writer.writeNext(header); 
		for (HashMap.Entry<String, String> entry : Gender_Map.entrySet()) {
			Object value=map.get(entry.getKey());
			String s= "";
					if(value==null) {
						 s="None";
					}else {
						 s =value.toString();
						s = s.substring(0, 1).toUpperCase() + s.substring(1);

					}
			
				  String[] body = {entry.getKey(),s }; 
			        writer.writeNext(body); 
				
				
				
				
		}
		writer.close();
Gender_Map.clear();
		
	}
	
	
	
	
	
}
