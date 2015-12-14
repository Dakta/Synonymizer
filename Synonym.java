package finalProject;

public class Synonym implements Comparable<Synonym> {
	
	public String synonym;
	public int frequency;
	public Synonym(String word) {
		this.synonym = word;
		this.frequency = 1;
	}
	
	public void increment() {
		this.frequency++;
	}
	
	@Override
	public boolean equals(Object other) {
	    if (other == null) return false;
	    if (other == this) return true;
		if (other instanceof String)  return other.equals(this.synonym);
		if (other instanceof Synonym) return ((Synonym) other).synonym.equals(this.synonym);
		return false;
	}
	
	@Override
	public int compareTo(Synonym o) {
		return ((Synonym) o).frequency - this.frequency;
	}
	
	@Override
	public String toString() {
	    return synonym + " - " + frequency;
	}
}
