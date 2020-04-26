
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.opencsv.CSVWriter;

public class csv_wr1 {
	

	
	
		public void do_funct() throws IOException {

        String csvFile1 = "deaths-train.csv";
        BufferedReader br1 = null;
        String line1 = "";
        String cvsSplitBy = ",";

        int counter=0;
        ArrayList<String> l=new ArrayList<String>();
        HashMap<String, String> names     	= new HashMap<String,String>();
            br1 = new BufferedReader(new FileReader(csvFile1));
            while ( (line1 = br1.readLine()) != null) {
            		counter=counter+1;
              
                String[] csv1 = line1.split(cvsSplitBy);
                	names.put(csv1[1],csv1[13]);
                	            }
            
            String csvFile = "train_pop.csv";
            BufferedReader br = null;
            String line = "";
            File file1 = new File("predict_train.csv"); 
            FileWriter outputfile1 = new FileWriter(file1); 
            CSVWriter writer = new CSVWriter(outputfile1);
                br = new BufferedReader(new FileReader(csvFile));
                int counte=0;
                while ( (line = br.readLine()) != null) {
               	 String[] csv = line.split(cvsSplitBy);
                	if(counte==0) {
                		String[] s= {csv[0].substring(1,(csv[0].length()-1)),csv[1].substring(1,(csv[1].length()-1)),csv[2].substring(1,(csv[2].length()-1)),"DwD"};
                		writer.writeNext(s);counte=counte+1;
                		continue;
                	}
                	 String[] s= {csv[0].substring(1,(csv[0].length()-1)),csv[1].substring(1,(csv[1].length()-1)),csv[2].substring(1,(csv[2].length()-1)),names.get(csv[0].substring(1,(csv[0].length()-1)))};
                		writer.writeNext(s);
                    
                			
                }
            writer.close();
            br.close();
            br1.close();
            
            
	        
	        
}}
