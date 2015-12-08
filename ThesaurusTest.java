package finalProject;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ThesaurusTest {

	private static boolean setUpIsDone = false;
	private static Thesaurus ths;
	
	@Before
	public void setUp() throws Exception {
	    if (setUpIsDone) {
	        return;
	    }
	    // do the setup
	    ths = new Thesaurus();
//	    System.out.println(ths);
	    
	    setUpIsDone = true;
	}

	@Test
	public void testSynonymEquality() {
		Synonym syn = new Synonym("feline");
		assertTrue(syn.equals(syn));
		assertTrue(syn.equals("feline"));
	}

//	@Test
//	public void testEntryGetSynonym() {
//		System.out.println(ths.entries.get("best").getSynonym());
//	}
	
	@Test
	public void testConjugateAll() {
	    ths.conjugateAllWords();
        System.out.println(ths.entries.get("bested").getLongSynonym());
	}
	
	@Test
	public void testGetConjugatedSynonym() {
	    System.out.println(ths.getSynonym("flying"));
	    System.out.println(ths.getSynonym("hosting"));
        System.out.println(ths.getSynonym("feelings"));
	}
}
