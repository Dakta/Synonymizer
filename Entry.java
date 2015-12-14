package finalProject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Entry {
	
	public String word;
	public ArrayList<Definition> definitions;
	public ArrayList<Synonym> synonymFrequency;
	public ArrayList<Affixer.Affix> affixes;
	public HashMap<String, Affixer.Affix> forms;
	
	public Entry(String word) {
		this.word = word;
		this.definitions = new ArrayList<Definition>();
		this.affixes = new ArrayList<Affixer.Affix>();
		this.forms = new HashMap<String, Affixer.Affix>();
	}
	public void addDefinition(Definition definition) {
		definitions.add(definition);
	}
	public void addAffix(Affixer.Affix afx) {
	    if (afx != null) affixes.add(afx);
	}
	
	@Override
	public String toString() {
		String ret = word + "\n";
        for (Affixer.Affix afx : affixes) {
            ret += afx.id + ", ";
        }
        ret += "\n";
		for (Definition def : definitions) {
			ret += def.toString();
		}
		return ret;
	}
	
	public Boolean contains(String synonym) {
		for (Definition def : definitions) {
			for (String syn : def.synonyms) {
				if (syn.equals(synonym)) return true;
			}
		}
		return false;
	}
	
	public String getLongSynonym() {
		if (definitions.size() == 0) {
			return null;
		}
		if (this.synonymFrequency == null) {
			synonymFrequency = new ArrayList<Synonym>();
			for (Definition def : definitions) {
				for (String syn : def.synonyms) {
				    // ignore shorter synonyms
				    if (syn.length() < this.word.length()) continue;
					// insert or increment
					int i;
					if ((i = synonymFrequency.indexOf(new Synonym(syn))) != -1) {
						synonymFrequency.get(i).increment();
					} else {
						synonymFrequency.add(new Synonym(syn));
					}
//					synonymFrequency.compute(syn, (a, b) -> (b==null) ? 1 : b + 1);
				}
			}
//			for (java.util.Map.Entry<String, Integer> entry : synonymFrequency.entrySet()) {
//				//
//			}
			Collections.sort(synonymFrequency);
//			for (Synonym syn : synonymFrequency) {
//				System.out.println(syn.frequency + " - " + syn.synonym);
//			}
		}
		// oops, we're the longest synonym
		if (synonymFrequency.size() < 1) return this.word;

		// we're smart, return the most frequent synonym		
		if (synonymFrequency.get(0).frequency < 2) {
		    // oops, all synonyms are equally frequent
		    // so just get the first one for now
		    // TODO: get the longest synonym
		    for (String syn : definitions.get(0).synonyms) {
		        
		    }
		    return definitions.get(0).synonyms.get(0);
		} else {
		    //
		    return synonymFrequency.get(0).synonym;
		}
		// we're dumb, just return first synonym of first definition
//		return definitions.get(0).synonyms.get(1);
	}
}
