
import java.io.*; 

public class FileMerge {
	
	public  void do_file(String file) throws IOException {
		File dir = new File(file); 
		  
        // create obejct of PrintWriter for output file 
        PrintWriter pw = new PrintWriter("output.txt"); 
  
        // Get list of all the files in form of String Array 
        String[] fileNames = dir.list(); 
        
        // loop for reading the contents of all the files  
        // in the directory GeeksForGeeks 
        for (String fileName : fileNames) { 
            System.out.println("Reading from " + fileName); 
  
            // create instance of file from Name of  
            // the file stored in string Array 
            File f = new File(dir, fileName); 
  
            // create object of BufferedReader 
            BufferedReader br = new BufferedReader(new FileReader(f)); 
            //pw.println("Contents of file " + fileName); 
  
            // Read from current file 
            String line = br.readLine(); 
            while (line != null) { 
  
                // write to the output file 
                pw.println(line); 
                line = br.readLine(); 
            } 
            pw.flush(); 
        } 
        System.out.println("Reading from all files" +  
        " in directory " + dir.getName() + " Completed"); 
		
		
		
	}
		
		
	
		

}
