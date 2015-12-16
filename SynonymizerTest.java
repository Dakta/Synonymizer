package finalProject;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SynonymizerTest {
        
    @Before
    public void setUp() throws Exception {
    }
    
    @Test
    public void test() throws Exception {
        Stopwatch st = new Stopwatch();
        Synonymizer.main(new String[] {"src/finalProject/PhantomTollbooth.txt"});
        System.out.println("# Replaced Phantom Tollbooth in " + st.elapsedNanos() + " nanoseconds.");
    }
    
}
