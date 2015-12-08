package finalProject;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class AffixerTest {
    
    private static boolean setUpIsDone = false;
    private static Affixer afxr;
    
    @Before
    public void setUp() throws Exception {
        if (setUpIsDone) return;
        
        // do the setup
        afxr = new Affixer();
        
        setUpIsDone = true;
    }
    
    @Test
    public void test() {
//        System.out.println(afxr);
        // armor/ZGMDRS
        System.out.println(afxr.applyRules('Z', "armor"));
        System.out.println(afxr.applyRules('G', "armor"));
        System.out.println(afxr.applyRules('M', "armor"));
        System.out.println(afxr.applyRules('D', "armor"));
        System.out.println(afxr.applyRules('R', "armor"));
        System.out.println(afxr.applyRules('S', "armor"));
        // arm/EAGDS
        System.out.println(afxr.applyRules('E', "arm"));
        System.out.println(afxr.applyRules('A', "arm"));
        System.out.println(afxr.applyRules('G', "arm"));
        System.out.println(afxr.applyRules('D', "arm"));
        System.out.println(afxr.applyRules('S', "arm"));
    }
    
}
