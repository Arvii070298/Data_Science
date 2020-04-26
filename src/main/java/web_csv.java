
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class web_csv {
	public static HashMap<String, String> read_csv(String file) throws IOException {
		HashMap<String, String> csv_map = new HashMap<String, String>();
		ArrayList<String> list= new ArrayList<String>();
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		int counter = 0;
		br = new BufferedReader(new FileReader(file));
		while ((line = br.readLine()) != null) {
			if (counter == 0) {
				counter = counter + 1;
				continue;
			}
			counter = counter + 1;
			String[] data = line.split(cvsSplitBy);
			if (data[1] == null) {

			} else {
				String x = data[1];
				
				csv_map.put(data[1],data[0]);
			}
			
		}
		br.close();

		return csv_map;

	}

	public static void main(String args[]) throws IOException {

		web_csv obj = new web_csv();
		HashMap<String, String> csv_map = new HashMap<String, String>();
		csv_map=obj.read_csv(null);

		for (Entry<String, String> entry : csv_map.entrySet()) {
			System.out.println(entry.getKey());
			
		}

		
	}

}
