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
        if(frame_1 == null || frame_2 == null){
            System.out.println("Tracking."+new String((frame_1 == null)? "frame_1":"frame_2")+" is null");
        }else{
            double[] matchLevels = new double[f.getShapes().size()];
            BufferedImage trackedSource = t.toImage();
            for(int sh = 0; sh < f.getShapes().size();sh++){
                Shape s = f.getShapes().get(sh);
                BufferedImage s_source = s.getTemplate().toImage(),
                        rescaled = new BufferedImage(trackedSource.getWidth(),trackedSource.getHeight(),BufferedImage.TYPE_INT_RGB);
                Graphics g = rescaled.createGraphics();
                g.drawImage(s_source, 0,0,trackedSource.getWidth(),trackedSource.getHeight(),null);
                g.dispose();
                double lMatchLevels = 0;
                for(int y = 0; y < trackedSource.getHeight();y++){
                    double lineMatchLevel = 0;
                    for(int x = 0; x < trackedSource.getWidth();x++){
                        if(trackedSource.getRGB(x,y) == rescaled.getRGB(x,y)){
                            lineMatchLevel += 1;
                        }
                    }
                    
                    lMatchLevels += lineMatchLevel/(trackedSource.getHeight()*trackedSource.getWidth());
                    
                }
                matchLevels[sh] = lMatchLevels;
            }

            return matchLevels;
        }
        return new double[] {-1};
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
        double highest = 0;
        int index = 0;
        for(int i = 0; i < matches.length;i++){
            double d = matches[i];
            //System.out.println("_"+matches[i]);
            if(d > highest){
                //System.out.println(d+" > "+highest);
                index = i;
                highest = d;
            }
        }
        //System.out.println(highest);
        Shape s = f.getShapes().get(index);
        return s;
    }
    
    public void setFirstFrame(Frame f){
        frame_1 = f;
    }
    public void setLastFrame(Frame f){
        frame_2 = f;
    }
    
    public void nextFrame(Frame frame){
        frame_1 = frame_2;
        frame_2 = frame;
    }
    
    public Frame getLastFrame(){
        return frame_2;
    }
    public Frame getFirstFrame(){
        return frame_1;
    }
    
}
