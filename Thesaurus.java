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
	    Stopwatch st = new Stopwatch();
	    this.afxr = new Affixer();
        System.out.println("# ✓ Loaded Affixer in " + st.elapsedNanos() + " nanoseconds.");
	    
		this.entries = new HashMap<String, Entry>();
	    this.conjugationRules = new HashMap<String, Affixer.Affix>();

//		File thesaurusFile = new File("src/finalProject/short_ths.dat");
		File thesaurusFile = new File("src/finalProject/th_en_US_new.dat");
				
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
		    		   if (!word.equals(synonym)) {
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
		    	   word = splitLine[0].trim();
		    	   entry = new Entry(word);
		       }
		    }		    
		}
				

//      File dictFile = new File("src/finalProject/dict-en/en_US.dic");
		File dictFile = new File("src/finalProject/en_US_improved.dic");
                
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
	    Stopwatch st = new Stopwatch();
	    
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
        
        System.out.println("# ✓ Generated all affixed forms in " + st.elapsedNanos() + " nanoseconds.");
        System.out.println("#   - Generated " + conjugatedForms.size() + " words for a total of " + entries.size() + " entries.");
	}
	
	
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
                
        return Affixer.applyRules(afx, entry.getLongSynonym());
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
