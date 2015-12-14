package finalProject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import finalProject.Affixer.Affix;
import finalProject.Affixer.PART_OF_SPEECH;

public class Entry {
	
	public String word;
	public ArrayList<Definition> definitions;
	public ArrayList<Synonym> synonymFrequency;
	public ArrayList<Affixer.Affix> affixes;
	public HashMap<String, Affixer.Affix> forms;
    private HashMap<Affixer.PART_OF_SPEECH, ArrayList<Synonym>> synonymFrequencyByPOS;
	
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
		
	
//	// unused!
//	public String getLongSynonymOfType(Affixer.PART_OF_SPEECH pos) {
//	    System.out.println(this);
//	    
//        if (definitions.size() == 0) {
//            return null;
//        }
//        // oop, gotta build it!
//        if (this.synonymFrequencyByPOS == null) {
//            synonymFrequencyByPOS = new HashMap<Affixer.PART_OF_SPEECH, ArrayList<Synonym>>();
//            ArrayList<Synonym> synonymFreq;
//            for (Definition def : definitions) {
//                for (String syn : def.synonyms) {
//                    // ignore shorter synonyms
//                    if (syn.length() < this.word.length()) continue;
//                    // find the synonym listing for this part of speech
//                    synonymFreq = synonymFrequencyByPOS.get(def.partOfSpeech);
//                    if (synonymFreq == null) {
//                        synonymFreq = new ArrayList<Synonym>();
//                        synonymFrequencyByPOS.put(def.partOfSpeech, synonymFreq);
//                    }
//                    // insert or increment this synonym
//                    int i;
//                    if ((i = synonymFreq.indexOf(new Synonym(syn))) != -1) {
//                        synonymFreq.get(i).increment();
//                    } else {
//                        synonymFreq.add(new Synonym(syn));
//                    }
//                }
//            }
//            // now sort the synonym frequencies for each part of speech
//            for (ArrayList<Synonym> synonymFrequency : synonymFrequencyByPOS.values()) {
//                Collections.sort(synonymFrequency);
//            }
//        }
//
//        for (java.util.Map.Entry<PART_OF_SPEECH, ArrayList<Synonym>> entry : synonymFrequencyByPOS.entrySet()) {
//            System.out.println(entry.getKey());
//            for ( Synonym syn : entry.getValue()) {
//                System.out.println(syn);
//            }
//        }
//
//        ArrayList<Synonym> synonymFreq = this.synonymFrequencyByPOS.get(pos);
//        if (synonymFreq == null) return this.word;
//
//        // oops, we're the longest synonym
//        if (synonymFreq.size() < 1) return this.word;
//
//        // we're smart, return the most frequent synonym
//        // TODO: get the longest synonym from the group of equal largest frequencies
//        if (synonymFreq.get(0).frequency < 2) {
//            // oops, all synonyms are equally frequent
//            // so just get the first one for now
//            String longestSynonym = this.word;
//            int longestSynonymLength = this.word.length();
//            for (Synonym syn : synonymFreq) {
//                if (syn.synonym.length() >= longestSynonymLength) {
//                    longestSynonym = syn.synonym;
//                    longestSynonymLength = syn.synonym.length();
//                }
//            }
//            return longestSynonym;
//        } else {
//            // there's a most-frequent
//            return synonymFrequency.get(0).synonym;
//        }
//	}
	
	public String getLongSynonym() {
//        System.out.println(this);

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
				}
			}
			Collections.sort(synonymFrequency);
//			for (Synonym syn : synonymFrequency) {
//				System.out.println(syn);
//			}
		}

//        for (int i = 0; i < synonymFrequency.size(); i++) {
//            System.out.println(synonymFrequency.get(i));
//        }

        // oops, we're the longest synonym
		if (synonymFrequency.size() < 1) return this.word;

		// we're smart, return the most frequent synonym		
		if (synonymFrequency.get(0).frequency < 2) {
		    // oops, all synonyms are equally frequent
		    // so just get the first one for now
		    // TODO: get the longest synonym
//		    for (String syn : definitions.get(0).synonyms) {
//		        System.out.println(syn);
//		    }
		    return definitions.get(0).synonyms.get(0);
		} else {
//		    System.out.println("wat");
		    //
		    return synonymFrequency.get(0).synonym;
		}
		// we're dumb, just return first synonym of first definition
//		return definitions.get(0).synonyms.get(1);
	}
}
