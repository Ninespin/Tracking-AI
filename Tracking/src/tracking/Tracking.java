package tracking;

import Shapes.Shape;
import Shapes.Template;
import graphics.Frame;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author jeremi
 */
public class Tracking {
    private Template tracked;
    private Frame frame_1,frame_2; 
    
    public Tracking(Template tracked){
        this.tracked = tracked;
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void init(String[] args) {
        
    }
    /*
        gets all the shapes in the frame, rescale thier image in a buffer, compare buffer
        with templates' image, returns an array containing n doubles (0 <= m <= 1), one per shape, 
        which contain the match percent
    */
    public double[] compareWithTemplate(){
        return compareWithTemplate(tracked,frame_2);
    }
    public double[] compareWithTemplate(Template t, Frame f){
        double[] matchLevels = new double[f.getShapes().size()];
        BufferedImage trackedSource = t.toImage();
        for(int sh = 0; sh < f.getShapes().size();sh++){
            Shape s = f.getShapes().get(sh);
            BufferedImage s_source = s.getTemplate().toImage(),
                    rescaled = new BufferedImage(trackedSource.getWidth(),trackedSource.getHeight(),BufferedImage.TYPE_INT_RGB);
            Graphics g = rescaled.createGraphics();
            g.drawImage(s_source, 0,0,trackedSource.getWidth(),trackedSource.getHeight(),null);
            g.dispose();
            
            for(int y = 0; y < trackedSource.getHeight();y++){
                double lineMatchLevel = 0;
                for(int x = 0; x < trackedSource.getWidth();x++){
                    if(trackedSource.getRGB(x,y) == rescaled.getRGB(x,y)){
                        lineMatchLevel += 1/trackedSource.getWidth();
                    }
                }
                matchLevels[sh] = lineMatchLevel/trackedSource.getHeight();
            }
        }
        
        return matchLevels;
    }
    
    /*
        returns the shapes whom has the highest match level
        uses compareWithTemplate()
    */
    public Shape getHighestMatch(){
        return getHighestMatch(tracked,frame_2);
    }
    public Shape getHighestMatch(Template t, Frame f){
        double[] matches = this.compareWithTemplate();
        int highest = 0;
        for(int i = 0; i < matches.length;i++){
            double d = matches[i];
            if(d > highest){
                highest = i;
            }
        }
        
        Shape s = f.getShapes().get(highest);
        return s;
    }
    
    
    public void nextFrame(Frame frame){
        frame_1 = frame_2;
        frame_2 = frame;
    }
    
    
}
