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
		
//		System.out.println(ths);
		
		Reader inputReader;
		if (args.length > 0) {
			inputReader = new FileReader(new File(args[0]));
		} else {
			inputReader = new InputStreamReader(System.in);
		}
		
		String line;
		String[] words;
		String punctuation;
		Pattern punctuationSplitter = Pattern.compile("(.*)([^\\w+]?)$");
		Matcher punctuationMatcher;
		try (BufferedReader br = new BufferedReader(inputReader)) {
		    while ((line = br.readLine()) != null) {
		    	words = line.split("\\s+"); // split on white space
		    	for (String word : words) {
		    		word = word.toLowerCase();
		    		
		    		punctuationMatcher = punctuationSplitter.matcher(word);
		    		if (punctuationMatcher.matches()) {
			    		word = punctuationMatcher.group(1);
			    		if (punctuationMatcher.groupCount() > 2) {
			    			punctuation = punctuationMatcher.group(2);
			    		} else {
			    			punctuation = "";
			    		}
		    		} else {
		    			punctuation = "";
		    		}

//		    		word = word.trim(); // we shouldn't have just whitespace, should we?
//		    		String punctuation = word.replaceAll(".*([^\\w]+)$", "$1");
		    		if (word.equals("")) continue; // skip empty entries
		    		// look up word in Thesaurus
		    		Entry entry = ths.entries.get(word);
		    		// if found:
		    		if (entry != null) {
		    			// print synonym
		    		    if (entry.word.equals(word)) {
		    		        System.out.print(entry.getLongSynonym());
		    		    } else {
		    		        System.out.print(ths.getSynonym(word));
		    		    }
//		    			System.out.print(entry.getSynonym());
		    		} else {
		    			// print word
		    			System.out.print(word);
		    		}
		    		System.out.print(" ");
		    	}
		    	// print a new line
		    	System.out.println();
		    }
		}
		
	}
}

