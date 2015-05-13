package tracking;

import IO.FrameStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            FrameStream fs = new FrameStream("");// <-- le path
            
            fs.setOutput(null);
            fs.start();
        } catch (IOException ex) {
            System.out.println("OOOOPs");
        }
        
        
        
    }
    
    
    
    
}
