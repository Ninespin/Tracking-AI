package tracking;

import IO.ShapeWriter;
import Shapes.Template;

/**
 *
 * @author jeremi
 */
public class Tracking {

    /**
     * @param args the command line arguments
     */
    public static void init(String[] args) {
        ShapeWriter sw = new ShapeWriter("C:\\Users\\eloi\\Documents\\ArnaudDossiers\\Prog\\Templates");
        for (int i = 0; i < 10; i++) {
            sw.save(new Template(), "file"+i);
        }
        sw.start();
    }
    
    
    
    
}
