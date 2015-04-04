package prerna.satija.code;

/* Author: Prerna Satija
 * Main program that takes list of variables and scores as input
 */ 
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class MainServer {

	public static void main(String[] args) {
		
		readData("sample.txt", "index.ser");
		
		IndexConstruction index = new IndexConstruction();
		index.deserialize("index.ser");
		QueryServer queryServer = new QueryServer(index);
		String wantMore = "y";
		//taking query input from user
		Scanner s = new Scanner(System.in);
		while(wantMore.equals("y")) {
			System.out.println("Enter your query: ");
			String query = s.next();
			List<Integer> ids = queryServer.find(query);
			List<Pair> pairs = index.getSortedPairs(ids);
			System.out.println("Top results for your query: ");
			for(int i = 0; i < 10 && i < pairs.size(); i++) {
				System.out.println(pairs.get(i));
			}
			//getting user input to continue or exit the search
			System.out.println("\nDo you want to search more? enter:y/n");
			wantMore = s.next();
		}
		s.close();
	}
	
	private static void readData(String filePath, String serializedTo) {
		IndexConstruction index = new IndexConstruction();
		try {
			String line = "";
			// reading sample file containing list of variable names and scores
			BufferedReader br = new BufferedReader(new FileReader("sample.txt"));
			while ((line = br.readLine()) != null) {
				String[] variablePairs = line.split(",");
				if(variablePairs.length == 2) {
					String variable = variablePairs[0];
					int score = Integer.parseInt(variablePairs[1]);
					index.add(variable, score);
				}
				else {
					System.out.println(line);
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		index.serialize("index.ser");
	}
}
