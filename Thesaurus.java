package finalProject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;

public class Thesaurus {
		
	public HashMap<String, Entry> entries;
	public HashMap<String, Affixer.Affix> conjugationRules;
	
	public Affixer afxr;

	public void addEntry(String word, Entry entry) {
		entries.put(word, entry);
	}
	
	public Thesaurus() throws Exception {
	    this.afxr = new Affixer();
	    
		this.entries = new HashMap<String, Entry>();
	    this.conjugationRules = new HashMap<String, Affixer.Affix>();

		File thesaurusFile = new File("src/finalProject/th_en_US_new.dat");
//		File thesaurusFile = new File("src/finalProject/short_ths.dat");
				
		try (BufferedReader br = new BufferedReader(new FileReader(thesaurusFile))) {
			// skip first line, we'll deal with character encoding oddities later
			br.readLine();
		    String line;
		    String[] splitLine;
		    String word = "";
		    Entry entry = new Entry("");
		    Definition dfn;
		    String synonym;
		    while ((line = br.readLine()) != null) {
	    	   splitLine = line.split("\\|");
		       if (line.startsWith("(")) {
		    	   dfn = new Definition(Affixer.parsePartOfSpeech(splitLine[0].replaceAll("\\s*\\((.*)\\)\\s*", "$1")));
		    	   for (int i = 1; i < splitLine.length; i++) {
		    		   synonym = splitLine[i].trim();
//		    		   System.out.println(synonym);
		    		   if (word.equals(synonym)) {
//		    			   System.out.print("equal:");
//		    			   System.out.println(word + " - " + synonym);
		    		   } else {
		    			   dfn.addSynonym(synonym);
		    		   }
		    	   }
		    	   if (dfn.synonyms.size() > 0) {		    		   
		    		   entry.addDefinition(dfn);
		    	   }
		       } else {
		    	   if (entry.definitions.size() > 0) {		    		   
		    		   this.addEntry(word, entry);
		    	   }
//		    	   System.out.println(entry);
		    	   word = splitLine[0].trim();
		    	   entry = new Entry(word);
		       }
		    }
		    
//		    this.removeUnidirectionalSynonyms();
		}
				
//		for (java.util.Map.Entry<String, Entry> e : this.entries.entrySet()) {
//			System.out.println(e.getValue());
//		}

        File dictFile = new File("src/finalProject/dict-en/en_US.dic");
                
        try (BufferedReader br = new BufferedReader(new FileReader(dictFile))) {
            br.readLine(); // skip first line; it's just a count of file length
            String line;
            String[] splitLine;
            String word = "";
            Entry entry;
            while ((line = br.readLine()) != null) {
                splitLine = line.split("/");
                if (splitLine.length != 2) continue;
                
                word = splitLine[0];
                entry = entries.get(word);
                if (entry == null) continue;
                for (int i = 0; i < splitLine[1].length(); i++) {
                    entry.addAffix(afxr.getAffix(splitLine[1].charAt(i)));
                }
            }
        }
	}
	
	public void conjugateAllWords() {
        HashMap<String, Entry> conjugatedForms = new HashMap<String, Entry>();
	    
        for (java.util.Map.Entry<String, Entry> ent : this.entries.entrySet()) {
            Entry entry = ent.getValue();
            String conjugation;
            for (Affixer.Affix afx : entry.affixes) {
                // construct the form
                conjugation = Affixer.applyRules(afx, entry.word);
                // add it to the entry
                entry.forms.put(conjugation, afx);
                // don't overwrite duplicates from other stems
                // because the thesaurus entry is more correct than the generated form
                if (!entries.containsKey(conjugation)) {
                    conjugatedForms.put(conjugation, entry);
                    // TODO: error checking on this for duplicate conjugations
                    conjugationRules.put(conjugation, afx);
                } else {
                    // System.out.println("Duplicate conjugation: " + conjugation + ", already have "+ entries.get(conjugation));
                }
            }
        }
        // We already duplicate checked this
        entries.putAll(conjugatedForms);
	}
	
//	public void linkAllSynonyms() {
//	    
//	}

	public String getSynonym(String word) {
	    Entry entry = entries.get(word);
	    Affixer.Affix afx = conjugationRules.get(word);
	    // for "unconjugated" words:
	    if (afx == null) return word;
	    
	    String syn = entry.getLongSynonym();
	    return Affixer.applyRules(afx, syn);
	}
	
	public String getSynonymOfType(String word) {
        Entry entry = entries.get(word);
        Affixer.Affix afx = conjugationRules.get(word);
        // for "unconjugated" words:
        if (afx == null) return word;
        
        return entry.getLongSynonymOfType(afx.pos);
	}

	
	/**
	 * removes synonyms which do not have the parent entry as a synonym.
	 * Theoretically this removes "weak" synonyms
	 */
	public void removeUnidirectionalSynonyms() {
		Iterator<java.util.Map.Entry<String, Entry>> h = this.entries.entrySet().iterator();
		Entry ent;
		while (h.hasNext()) {
			ent = h.next().getValue();
			String search = ent.word;
			
			// search synonyms for binary synonym
			Definition def;
			Iterator<Definition> j = ent.definitions.iterator();
			while (j.hasNext()) {				
				def = j.next();
				String syn;
				Iterator<String> i = def.synonyms.iterator();
				while (i.hasNext()) {
					syn = i.next();
					if (!this.entries.get(search).contains(search)) {
						// we don't have a bi-directional match, so drop it
						i.remove();
					}
				}
				if (def.synonyms.size() == 0) {
					// drop definitions with no synonyms
					j.remove();
				}
			}
			if (ent.definitions.size() == 0) {
				// drop words with no definitions
				h.remove();
			}
		}

	}
	
	/**
	 * NEVER DO THIS
	 * (It will take many minutes to return.)
	 * Use printEntries() instead
	 * @return
	 */
	@Deprecated
	@Override
	public String toString() {
		String repr = "";
		for (java.util.Map.Entry<String, Entry> entry : this.entries.entrySet()) {
//			repr = entry.getKey() + ":\n";
			System.out.println(entry);
			repr += entry.getValue().toString();
		}
		return repr;
	}

    public void printEntries() {
        for (java.util.Map.Entry<String, Entry> entry : this.entries.entrySet()) {
            System.out.println(entry.getValue());
        }
    }

}
