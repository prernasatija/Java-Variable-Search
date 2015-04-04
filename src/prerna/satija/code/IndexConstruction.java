package prerna.satija.code;
//Author: Prerna Satija

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IndexConstruction {
	
	private ArrayList<Pair> pairIndex;
	
	public IndexConstruction () {
		pairIndex = new ArrayList<>();
	}
	
	//adds variable name and score pairs to the index structure
	public void add(String name, Integer score) {
		pairIndex.add(new Pair(pairIndex.size(), name, score));
	}
	
	public int size() {
		return pairIndex.size();
	}
	
	public Pair get(int id) {
		return pairIndex.get(id);
	}
	
	public String getString(int id) {
		return pairIndex.get(id).getName();
	}
	
	// gives a list of sorted variable pairs based on score
	public List<Pair> getSortedPairs(List<Integer> ids) {
		ArrayList<Pair> pairs = new ArrayList<>();
		for(Integer id : ids) {
			pairs.add(get(id));
		}
		Collections.sort(pairs);
		return pairs;
	}
	
	// reads pairs from filepath and populates the index structure
	@SuppressWarnings("unchecked")
	public void deserialize(String filepath) {
		try {
			FileInputStream fileIn = new FileInputStream(filepath);
	        ObjectInputStream in = new ObjectInputStream(fileIn);
	        Object o = in.readObject();
	        if( o instanceof ArrayList) {
	        	this.pairIndex = (ArrayList<Pair>)o;
	        }
	        in.close();
	        fileIn.close();
		} catch (IOException i) {
			i.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//writes pairs into a file in the location specified by the filepath
	public void serialize(String filepath) {
		try
		{
			FileOutputStream fileOut =new FileOutputStream(filepath);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this.pairIndex);
			out.close();
			fileOut.close();
			System.out.printf("Serialized data is saved in "+filepath + "\n");
		}catch(IOException i)
		{
			i.printStackTrace();
		}
	}

}

