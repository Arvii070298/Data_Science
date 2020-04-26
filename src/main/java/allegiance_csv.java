package assign1;

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

public class allegiance_csv {
	public static ArrayList<String> house_names = new ArrayList<String>();

	public static HashMap<String, String> reader(String csvFile) throws IOException {
		HashMap<String, String> Gender_Map = new HashMap<String, String>();
	//	csvFile = "C:/Users/Sai Arvind/eclipse-workspace/assign1/deaths-train.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		int counter = 0;
		br = new BufferedReader(new FileReader(csvFile));
		while ((line = br.readLine()) != null) {
			if (counter == 0) {
				counter = counter + 1;
				continue;
			}
			counter = counter + 1;
			String[] data = line.split(cvsSplitBy);
			String x=data[2];
			if(data[2].contains("House")) {
				String q[]=data[2].split(" ");
				x=q[1];
			}

			Gender_Map.put(data[1], x);
			if (!house_names.contains(x)) {
				house_names.add(x);
			}
		}
		br.close();
		return Gender_Map;
	}
	


}