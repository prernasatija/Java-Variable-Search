package prerna.satija.code;

//Author: Prerna Satija
import java.io.Serializable;

public class Pair implements Comparable<Object>, Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private Integer score;
	
	public Pair(int id, String name, int score) {
		this.id = id;
		this.name = name;
		this.score = score;
	}
	
	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getScore() {
		return score;
	}

	// Compares the variable pair objects by score
	@Override
	public int compareTo(Object o) {
		if(!(o instanceof Pair)) {
			return -1;
		}
		Pair p = (Pair)o;
		return p.score.compareTo(this.score);
	}
	
	// overrides the to_string method of Object class
	public String toString() {
		return name + " : " + score;
	}
	
}