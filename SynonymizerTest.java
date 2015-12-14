package finalProject;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SynonymizerTest {
    
    private static boolean setUpIsDone = false;
    private static Thesaurus ths;
    
    @Before
    public void setUp() throws Exception {
        if (setUpIsDone) {
            return;
        }
        // do the setup
        ths = new Thesaurus();
//      System.out.println(ths);
        
        setUpIsDone = true;
    }
    
    @Test
    public void test() {
        fail("Not yet implemented");
    }
    
}
