package assign1;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Rule_based {
	public static void rule_based(String file_book) {

		String csvFile = file_book;
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		String[] file_data = null;
		HashMap<String, ArrayList<String>> names_vals = new HashMap<String,ArrayList<String>>();
int cr=0;int even=0;
		HashMap<String, String> char_deaths = new HashMap<String,String>();
		int counter=1;
		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				file_data = line.split(cvsSplitBy);
				if(counter==1) {
					counter=0;
					continue;
				}
				int count_books=0;
				ArrayList<String> List = new ArrayList<String>();

				// use comma as separator

				String house_names=file_data[14];
				String character_names=file_data[5];
				String gender=file_data[7];
				String Noble=file_data[26];
				String age=file_data[27];
				String popularity=file_data[31];
				
				char_deaths.put(character_names, file_data[32]);
				if(file_data[32].equals("0") && even%2==0) {
					cr=cr+1;
				}
				even=even+1;
				
				// List.add(file_data[7]);

				if(file_data[14].equals("")) {
					house_names="N/A";
				}
				if(file_data[27].equals("")){
					age="N/A";
				}
				count_books=Integer.parseInt(file_data[16].trim());
				count_books=count_books+Integer.parseInt(file_data[17]);
				count_books=count_books+Integer.parseInt(file_data[18]);
				count_books=count_books+Integer.parseInt(file_data[19]);
				count_books=count_books+Integer.parseInt(file_data[20]);
				//System.out.println(house_names+" "+file_data[5]+" "+file_data[7]+" "+count_books);
				List.add(house_names);
				List.add(gender);
				List.add(Noble);
				List.add(age);
				List.add((String.valueOf(count_books)));
				List.add(popularity);
				List.add(file_data[32]);
				names_vals.put(character_names, List);                

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		int total=0;int co=0;int dead=0;
		ArrayList<String> alive = new ArrayList<String>();
		for (Entry<String, ArrayList<String>> que: names_vals.entrySet()) {
			ArrayList<String> List = new ArrayList<String>();
			List=que.getValue();

			if(	List.get(1).equals("0") ) {
				alive.add(que.getKey());
				total=total+1;

			}else if(Float.valueOf(List.get(5))>.9 && (Integer.parseInt(List.get(4))>=3))
			{		
				alive.add(que.getKey());
				total=total+1;

			}else if(List.get(0).equals("N/A")){

				alive.add(que.getKey());
				total=total+1;
			}}
		for (int c = 0; c < alive.size(); c++) { 		      
			names_vals.remove(alive.get(c));

		} 
		double tp=0.0,fp=0.0;

		for (Entry<String, ArrayList<String>> que: names_vals.entrySet()) {

			if(char_deaths.get(que.getKey()).equals("0")) {
				tp=tp+1;
			}else {
				fp=fp+1;
			}
		}
		double fn=0.0;        
		for (Entry<String, String> que: char_deaths.entrySet()) {

			if(names_vals.get(que.getKey())==null && char_deaths.get(que.getKey()).equals("0")) {
				fn=fn+1;
			}
		}

		double prec=(tp/(fp+tp));
		double rec=(tp/(tp+fn));
		System.out.println("Prec: "+prec+"  "+"rec: "+rec);
		double f1=2*((prec*rec)/(prec+rec));

		System.out.println("F1 Measure: "+f1);




	}

}