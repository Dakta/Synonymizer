package finalProject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Synonymizer {

	public static void main(String[] args) throws Exception {
	    System.out.println("# Launching...");
	    Stopwatch st = new Stopwatch();
		Thesaurus ths = new Thesaurus();
		System.out.println("# ✓ Loaded Thesaurus data in " + st.elapsedNanos() + " nanoseconds.");
        ths.conjugateAllWords();
        System.out.println("# Fully Loaded Data Structures in " + st.elapsedNanos() + " nanoseconds.");
		System.out.println("# -------------------------------\n\n");
        
        // Get user input (text file or stdin)
		Reader inputReader;
		if (args.length > 0) {
			inputReader = new FileReader(new File(args[0]));
		} else {
			inputReader = new InputStreamReader(System.in);
		}
		
		int checkedWords = 0;
		int replacedWords = 0;
		
		String line;
		String[] words;
        String punctuationFore;
        String punctuationAft;
		Pattern punctuationSplitter = Pattern.compile("^(\\W+)?(.*)(\\W+)?$");
		Matcher punctuationMatcher;
		try (BufferedReader br = new BufferedReader(inputReader)) {
		    while ((line = br.readLine()) != null) {
		    	words = line.split("\\s+"); // split on white space
		    	for (String word : words) {
                    punctuationFore = "";
                    punctuationAft = "";

                    // separate word from any trailing punctuation
                    punctuationMatcher = punctuationSplitter.matcher(word);
		    		if (punctuationMatcher.matches()) {		    		    
		    		    punctuationFore = punctuationMatcher.group(1);
		    		    if (punctuationFore == null) punctuationFore = "";
                        word = punctuationMatcher.group(2);
                        punctuationAft = punctuationMatcher.group(3);
                        if (punctuationAft == null) punctuationAft = "";
		    		}
                    // we shouldn't actually lowercase stuff because proper nouns.. Oh well
                    boolean uppercase = word.matches("^[A-Z].*");
                    word = word.toLowerCase();

		    		if (word.equals("")) continue; // skip empty entries		    		
		    		// look up word in Thesaurus
		    		Entry entry = ths.entries.get(word);
		    		String synonym;
		    		// if found:
		    		if (entry != null && word.length() > 3) {
		    			// print synonym
		    		    if (entry.word.equals(word)) {
		    		        // no need to affix the synonym
		    		        // get the entry's longest synonym
		    		        synonym = entry.getLongSynonym();
		    		    } else {
		    		        // we need to affix the synonym
		    		        // thesaurus has a method for this
		    		        synonym = ths.getSynonymOfType(word);
		    		    }
                        replacedWords++;
		    		/// not found? just use what we have
		    		} else {
		    			// print word
		    			synonym = word;
		    		}
		    		
		    		// make uppercase
                    if (uppercase) {
                        synonym = synonym.substring(0, 1).toUpperCase() + synonym.substring(1);
                    }
                    // print punctuationFore
                    System.out.print(punctuationFore);
                    // print word
                    System.out.print(synonym);
                    // print punctuationAft
		    		System.out.print(punctuationAft);
		    		// and add trailing whitespace
		    		System.out.print(" ");

		    		checkedWords++;
		    	}
		    	// print a new line
		    	System.out.println();
		    }
		}
		System.out.println("\n");
		System.out.println("# Replaced " + replacedWords + " of " + checkedWords + " words (" + ((double)replacedWords/checkedWords * 100.0) + "%).");
	}
}

