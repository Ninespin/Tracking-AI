package tracking;

import IO.FrameStream;
import Shapes.PatternDetector;
import Shapes.Shape;
import Shapes.Template;
import graphics.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 *
 * @author Jérémi Cyr & Arnaud Paré-Vogt
 */
public class Tracking {
    private Template tracked;
    private Frame frame_1,frame_2; 
    private Point[] pts;
    private DisplacementVector d;
    
    private final Thread analyserThread;//analyses the shapes to give them an error percentage.
    private Runnable analyserRunnable;
    public enum CurrentAnalisisStatus{REFRESH,REFRESH_LAST_CYCLE,RECYCLE,FINISHED};
    private CurrentAnalisisStatus analisisStatus = CurrentAnalisisStatus.FINISHED;
    
    private boolean running = true;
    
    public Tracking(Template tracked, FrameStream fs){
        this.tracked = tracked;
        initThread();
        analyserThread = new Thread(analyserRunnable, "AnalyserThread");
    }
    
    private void initThread(){
        analyserRunnable = ()->{//analyse shapes
            int shapeIndex = 0;
            
            while(running){
                while(analisisStatus == CurrentAnalisisStatus.FINISHED){
                    synchronized(analyserThread){
                        try {
                            analyserThread.wait();
                        } catch (InterruptedException ex) {}
                    }
                }
                if(analisisStatus == CurrentAnalisisStatus.REFRESH){
                    shapeIndex = compareWithTemplate(tracked, frame_2, shapeIndex);
                }else if(analisisStatus == CurrentAnalisisStatus.RECYCLE){
                    compareWithTemplate(tracked, frame_2, shapeIndex);
                    shapeIndex = 0;
                    analyseFrame();
                    analisisStatus = CurrentAnalisisStatus.FINISHED;
                }
            }
        };
    }
    
    public void start(){
        analyserThread.start();
    }
    
    /*
        gets all the shapes in the frame, rescale thier image in a buffer, compare buffer
        with templates' image, returns an array containing n doubles (0 <= m <= 1), one per shape, 
        which contain the match percent
    */
    private void compareWithTemplate(){
        synchronized(analyserThread){
            analyserThread.notifyAll();
        }
    }
    private int compareWithTemplate(Template t, Frame f,int startIndex){
        if(frame_2 == null){
            System.out.println("Tracking."+new String((frame_1 == null)? "frame_1":"frame_2")+" is null");
        }else{
            BufferedImage trackedSource = t.toImage();
            int sh;
            for(sh = startIndex; sh < f.getShapes().size();sh++){
                System.out.println("iteration");
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
                f.setShapeMatch(sh, lMatchLevels);
            }
            f.setValidMatches(true);
            return sh;
        }
        System.out.println("noooo");
        return startIndex;
    }
    
    /**
     * returns the shapes whom has the highest match level
     * uses compareWithTemplate()
     */
    public Shape getHighestMatch(){
        if(frame_2 == null){
            System.out.println("frame 2 is null");
            return null;
        }
        return getHighestMatch(tracked,frame_2);
    }
    
    public Shape getHighestMatch(Template t, Frame f){
        double highest = 0;
        int index = 0;
        if(f.getShapes().size()>0){
            for(int i = 0; i < f.getShapes().size();i++){
                double db = f.getShapeMatch(i);
                //System.out.println("_"+matches[i]);
                if(db > highest){
                    //System.out.println(d+" > "+highest);
                    index = i;
                    highest = db;
                }
            }
            //System.out.println(highest);
        
            f.setTrackedShapeIndex(index);
            Shape s = f.getTrackedShape();
            compareTracked();
            return s;
        }
        return null;
    }
    
    public void compareTracked(){
        if(frame_1 != null && frame_2 != null){
            /*DISPL VECTOR*/
            d = new DisplacementVector(this.getFirstFrame(),this.getLastFrame());
            pts = d.getDisplacement();
            System.out.println(pts[0]+"\n"+pts[1]);
            /*END DISPL VECTOR*/
        }else if(frame_1 == null && frame_2 != null){
            d = new DisplacementVector(this.getLastFrame(),this.getLastFrame());
            pts = d.getDisplacement();
        }
    }
    
    public Point[] getPts(){
        if(pts == null){
            compareTracked();
        }
        return pts;
    }
    
    /*
        returns the tracked shape object of the la last frame
    */
    public DisplacementVector getDisplacementVector(){
        return d;
    }
    
    public void nextFrame(Frame f){
        frame_1 = frame_2;
        if(frame_2 == null)System.out.println("null2 bitch");
        frame_2 = f;
    }
    
    public Frame getLastFrame(){
        return frame_2;
    }
    public Frame getFirstFrame(){
        return frame_1;
    }
    
    /**
     * Analyses the lestest frame and stores all information in it.
     */
    public void analyseFrame(){
        if(frame_2 != null){
            getHighestMatch(tracked,frame_2);//Stores the highest match in frame_2
            if(frame_1 != null){
            }
        }
    }
    
    /**
     * Warns the <code>Tracking</code> object that changes might have occured and that it should check it's shape array.
     * @param status the current status
     */
    public void warn(PatternDetector.CurrentDetectStatus status){
        if(status == PatternDetector.CurrentDetectStatus.DETECTING_CHANGES_NOT_TRACKED){
            analisisStatus = CurrentAnalisisStatus.REFRESH;
            this.compareWithTemplate();
        }else if(status == PatternDetector.CurrentDetectStatus.FINISHED){
            analisisStatus = CurrentAnalisisStatus.RECYCLE;
            this.compareWithTemplate();
        }
    }
    
    
}
