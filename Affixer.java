package finalProject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * An affix is an additional element placed at the beginning
 * or end of a root, stem, or word, or in the body of a word,
 * to modify its meaning. See also infix, prefix, suffix.
 * 
 * This class represents affix rules used by Hunspell,
 * contained in .aff files.
 * 
 * @author dakota
 *
 */
public class Affixer {
	public static enum AFFIX_TYPE {
		PFX, SFX
	}
	public static AFFIX_TYPE parseType(String type) {
		return AFFIX_TYPE.valueOf(type.toUpperCase().trim());
	}
	
	public static enum PART_OF_SPEECH {
	    NOUN, VERB, ADJ, ADV
	}
	public static PART_OF_SPEECH parsePartOfSpeech(String pos) {
	    return PART_OF_SPEECH.valueOf(pos.toUpperCase().trim());
	}
	
	public class Affix {
		public class AffixRule {
			// TODO: implement this
			public String strip;
			public String match;
			public String replace;
			public AffixRule(String strip, String match, String replace) {
				this.strip = strip.trim();
				this.strip = (this.strip.equals("0") ? null : this.strip);
				
				this.match = match.trim();
				this.replace = replace.trim();
			}
			@Override
			public String toString() {
			    return strip + " " + match + " " + replace;
			}
		}

		public char id;
		public AFFIX_TYPE type;
        public PART_OF_SPEECH pos;
        public String combinations;

        ArrayList<AffixRule> rules;
		
		public Affix(String type, char id, String pos, String comb) {
			rules = new ArrayList<AffixRule>();

			this.id = id;
			this.type = parseType(type);
			this.pos = Affixer.parsePartOfSpeech(pos);
			this.combinations = comb;
		}
		
		public void addRule(AffixRule rule) {
		    rules.add(rule);
		}
		@Override
		public String toString() {
		    String ret = type + " " + id + " " + pos + " " + combinations + "\n";
		    for (AffixRule rule : rules) {
		        ret += rule + "\n";
		    }
		    return ret;
		}
	}
	
	public HashMap<Character, Affix> affixes;
	
	public Affixer() throws Exception {
		this.affixes = new HashMap<Character, Affix>();
		readAffixFile(new File("src/finalProject/en_US_improved.aff"));
	}

	public void readAffixFile(File f) throws Exception {
		/*
		 * See affDescription.txt for the aff file format
		 */
		
		// TODO: implement this
		Reader inputReader = new FileReader(f);
		String line;
		String[] splitLine;
		Character ruleID = null;
		Affix affix = null;
		try (BufferedReader br = new BufferedReader(inputReader)) {
		    while ((line = br.readLine()) != null) {		        
		        if (line.startsWith("PFX")
	    		    || line.startsWith("SFX")
		        ) {
                    splitLine = line.split("\\s+");
                    
                    if (ruleID == null || splitLine[1].charAt(0) != ruleID) {
                        // new affix entry
                        addAffix(affix);
                        ruleID = splitLine[1].charAt(0);
                        if (splitLine.length == 5) {
                            affix = new Affix(splitLine[0], ruleID, splitLine[2], splitLine[3]);
                        } else {
                            // we just assume that it forms a noun (wrong!) with plural/possessive
                            affix = new Affix(splitLine[0], ruleID, "noun", "SM");
                        }
                    } else {
                        // add rule to current entry
                        affix.addRule(affix.new AffixRule(splitLine[2], splitLine[4], splitLine[3]));
                    }
    		    } else {
    		        // we can ignore this line
    		        // example: REP, SET, TRY, <blank>, #comment
    		        continue;
    		    }
		    }
		}

	}
	public void readAffixFile(String path) throws Exception {
		readAffixFile(new File(path));
	}
	
	public void addAffix(Affix afx) {
		if (afx != null) affixes.put(afx.id, afx);
	}
	
	public Affix getAffix(char id) {
		return affixes.get(id);
	}

	public String applyRules(char id, String stem) {
	    return applyRules(getAffix(id), stem);
	}

	public static String applyRules(Affix afx, String stem) {
		// TODO: implement this
	    String result = stem;
		// get rules
		String pattern;
		Pattern p;
		Matcher m;
		for (Affix.AffixRule rule : afx.rules) {
//		    System.out.println(rule);
		    
		    pattern = "";
			switch (afx.type) {
			// regex match
			case PFX:
				pattern = "^(" + rule.match + ").*";
				break;
			case SFX:
				pattern = ".*(" + rule.match + ")$";
				break;
			}
			p = Pattern.compile(pattern);
//			System.out.println(p);
			
			m = p.matcher(stem);
//			System.out.println(m);
			
			if (m.matches()) {
//			    System.out.println("matches!");
			    
//				System.out.println(m.group(0));
				
	            // if rule.strip == null, don't strip
	            pattern = "";
//	            System.out.println(pattern);	            
	            switch (afx.type) {
	                // regex match
	                case PFX:
                        pattern = "^" + (rule.strip != null ? rule.strip : "") + "(.*)$";
	                    result = stem.replaceAll(pattern, rule.replace + "$1");
	                    break;
	                case SFX:
                        pattern = "^(.*)" + (rule.strip != null ? rule.strip : "") + "$";
	                    result = stem.replaceAll(pattern, "$1" + rule.replace);
	                    break;
	            }
			}
		}
		return result;
	}
	
	@Override
	public String toString() {
	   String ret = "";
	   for (Affix afx : affixes.values()) {
	       ret += afx.toString();
	       ret += "\n";
	   }
	   return ret;
	}

}
