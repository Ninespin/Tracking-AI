package tracking;

import IO.FrameStream;
import IO.ShapeWriter;
import Shapes.Template;
import graphics.Filter;
import graphics.FrameProcessor;
import java.awt.Color;
import java.io.IOException;

/**
 *
 * @author jeremi
 */
public class Tracking {

    /**
     * @param args the command line arguments
     */
    public static void init(String[] args) {
        ShapeWriter sw = new ShapeWriter("C:\\Users\\eloi\\Documents\\ArnaudDossiers\\Prog");
        for (int i = 0; i < 10; i++) {
            sw.print(new Template(), "file"+i);
        }
    }
    
    
    
    
}
