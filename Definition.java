package finalProject;

import java.util.ArrayList;

public class Definition {
	public String partOfSpeech;
	public ArrayList<String> synonyms;
	public Definition(String partOfSpeech) {
		this.partOfSpeech = partOfSpeech;
		this.synonyms = new ArrayList<String>();
	}
	public void addSynonym(String synonym) {
		synonyms.add(synonym);
	}
	@Override
	public String toString() {
		String ret = partOfSpeech + " - ";
		for (String syn : synonyms) {
			ret += syn + ", ";
		}
		ret += "\n";
		return ret;
	}
}
