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
		return (type.toLowerCase().trim().equals("sfx")) ? AFFIX_TYPE.SFX : AFFIX_TYPE.PFX;
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
		public boolean canPrefix;
		ArrayList<AffixRule> rules;
		
		public Affix(char id, String canPrefix, String type) {
			rules = new ArrayList<AffixRule>();
			this.type = parseType(type);
			this.id = id;
			this.canPrefix = canPrefix.toLowerCase().trim().equals("y");
		}
		
		public void addRule(AffixRule rule) {
		    rules.add(rule);
		}
		@Override
		public String toString() {
		    String ret = type + " " + id + " " + canPrefix + "\n";
		    for (AffixRule rule : rules) {
		        ret += rule + "\n";
		    }
		    return ret;
		}
	}
	
	public HashMap<Character, Affix> affixes;
	
	public Affixer() throws Exception {
		this.affixes = new HashMap<Character, Affix>();
		readAffixFile(new File("src/finalProject/dict-en/en_US.aff"));
	}

	public void readAffixFile(File f) throws Exception {
		/*
		 * ---------------------------------------------------------------------
		What follows is cut and pasted from the instructions at
		http://whiteboard.openoffice.org/lingucomponent/affix.readme
		
		The first line has 4 fields:
		
		Field
		-----
		1     SFX - indicates this is a suffix
		2     D   - is the name of the character which represents this suffix
		3     Y   - indicates it can be combined with prefixes (cross product)
		4     4   - indicates that sequence of 4 affix entries are needed to
		               properly store the affix information
		
		The remaining lines describe the unique information for the 4 affix
		entries that make up this affix.  Each line can be interpreted
		as follows: (note fields 1 and 2 are used as a check against line 1 info)
		
		Field
		-----
		1     SFX         - indicates this is a suffix
		2     D           - is the name of the character which represents this affix
		3     y           - the string of chars to strip off before adding affix
		                         (a 0 here indicates the NULL string)
		4     ied         - the string of affix characters to add
		                         (a 0 here indicates the NULL string)
		5     [^aeiou]y   - the conditions which must be met before the affix
		                    can be applied
		
		Field 5 is interesting.  Since this is a suffix, field 5 tells us that
		there are 2 conditions that must be met.  The first condition is that
		the next to the last character in the word must *NOT* be any of the
		following "a", "e", "i", "o" or "u".  The second condition is that
		the last character of the word must end in "y".
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
                        affix = new Affix(ruleID, splitLine[2], splitLine[0]);
                    } else {
                        // add rule to current entry
                        affix.addRule(affix.new AffixRule(splitLine[2], splitLine[4], splitLine[3]));
                    }
    		    } else {
    		        // we can ignore this line
    		        // example: REP, SET, TRY, <blank>
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
