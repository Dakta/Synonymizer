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
		Thesaurus ths = new Thesaurus();
        ths.conjugateAllWords();
				
		Reader inputReader;
		if (args.length > 0) {
			inputReader = new FileReader(new File(args[0]));
		} else {
			inputReader = new InputStreamReader(System.in);
		}
		
		String line;
		String[] words;
		String punctuation;
		Pattern punctuationSplitter = Pattern.compile("^(.*)([^\\w+]+)$");
		Matcher punctuationMatcher;
		try (BufferedReader br = new BufferedReader(inputReader)) {
		    while ((line = br.readLine()) != null) {
		    	words = line.split("\\s+"); // split on white space
		    	for (String word : words) {
		    	    // we shouldn't actually lowercase stuff because proper nouns
		    		// word = word.toLowerCase();
                    punctuation = "";

                    // separate word from any trailing punctuation
                    punctuationMatcher = punctuationSplitter.matcher(word);
		    		if (punctuationMatcher.matches()) {
			    		word = punctuationMatcher.group(1);
			    		if (punctuationMatcher.groupCount() > 1) {
			    			punctuation = punctuationMatcher.group(2);
			    		}
		    		}

		    		if (word.equals("")) continue; // skip empty entries
		    		// look up word in Thesaurus
		    		Entry entry = ths.entries.get(word);
		    		// if found:
		    		if (entry != null && word.length() > 3) {
		    			// print synonym
		    		    if (entry.word.equals(word)) {
		    		        // no need to affix the synonym
		    		        // get the entry's longest synonym
		    		        System.out.print(entry.getLongSynonym());
		    		    } else {
		    		        // we need to affix the synonym
		    		        // thesaurus has a method for this
		    		        System.out.print(ths.getSynonym(word));
		    		    }
		    		/// not found? just use what we have
		    		} else {
		    			// print word
		    			System.out.print(word);
		    		}
		    		// print punctuation
		    		System.out.print(punctuation);
		    		// and add trailing whitespace
		    		System.out.print(" ");
		    	}
		    	// print a new line
		    	System.out.println();
		    }
		}
	}
}

