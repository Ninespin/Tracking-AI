package tracking;

import IO.FrameStream;
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
    public static void main(String[] args) {
        try {
            FrameStream fs = new FrameStream("C:\\Users\\eloi\\Documents\\ArnaudDossiers\\Prog");// <-- le path
            Filter f = new Filter(Color.red,10);
            FrameProcessor fp = new FrameProcessor(f);
            fs.setOutput(fp);
            fs.start();
        } catch (IOException ex) {
            System.out.println("OOOOPs");
        }
        
        
        
    }
    
    
    
    
}
