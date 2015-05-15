/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shapes;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jérémi Cyr & Arnaud Paré-Vogt
 */
public class PatternDetector {

    private ArrayList<Shape> detectedShapes;
    private BufferedImage frame;
    private final int ERROR_MARGIN = 25;//in %
    private final int SMALLSHAPE_FACTOR = 100001;

    private ArrayList<Shape> exclude,discarded;
    private boolean detectShapeResult = true;
    private boolean detecting = false;

    private Thread mainThread;
    private boolean running = true;

    /**
     * Creates a new PatternDetector, used to detect patterns (duh) in a given <code>BufferedImage</code>. The pattens are named <code>Template</code> and the detected pattens <code>Shape</code>.
     * @param _frame The BufferedImage in whitch detect the patterns 
     */
    public PatternDetector(BufferedImage _frame) {
        frame = _frame;
        detectedShapes = new ArrayList<Shape>();
        initThreads();
    }

    /**
     * Used by the constructor to initialyse the threads that are used to detect the patterns.
     */
    private void initThreads() {
        mainThread = new Thread(() -> {
            while (running) {
                synchronized (mainThread) {
                    try {
                        synchronized(this){
                            this.notifyAll();
                        }
                        mainThread.wait();
                    } catch (InterruptedException ex) {
                    }
                }
                if(running){
                    detectShapeResult = detectShape(exclude,0);
                    exclude = null;
                }
            }
        }, "Shape Finder thread");
        mainThread.start();
        synchronized(this){
            try {
                this.wait();
            } catch (InterruptedException ex) {}
        }
    }
    
    /**
     * Stops the thread. Useful when you dump the object.
     */
    public void stop(){
        running = false;
        synchronized (mainThread) {
            mainThread.notifyAll();
        }
    }

    /**
     * Activates a dedicated thread to detect Templates and Shapes in the image of the <code>PatternDetector</code>
     * @param exclude An <code>ArrayList</code> of shapes that may be excluded from the search
     * @return if the current thread is already working on detecting the shapes
     */
    public boolean detectShapes(ArrayList<Shape> exclude) {
        if (detecting) {
            return true;
        } else {
            System.out.println("process!");
            this.exclude = exclude;
            synchronized (mainThread) {
                mainThread.notifyAll();
            }
            return false;
        }
    }

    /**
     * Detects the shapes in the frame on this object
     * @param exclude the shapes to exclude
     * @param startY the current y coordinate (this method is recursive)
     * @return i do not honestly know, and i don't think it matters much
     */
    //TODO clean the massive code and fragment it for redability. Seriously, it is more than 100 lines long!
    private boolean detectShape(ArrayList<Shape> exclude,int startY) {
        if (exclude == null) {
            exclude = new ArrayList<Shape>();
        }
        if (discarded == null) {
            discarded = new ArrayList<Shape>();
        }
        int[] centerY = {-1, -1};//x,y
        int xMax, xMin, yMax, yMin;
        boolean x0Stop = false, x1Stop = false, y1Stop = false;

        int xPos=0,yPos=0;
        System.out.println("Looking...");
        //first encounter  ** missing -> check if point is in not exclude
        for (yPos = startY; yPos < frame.getHeight(); yPos++) {
            for (xPos = 0; xPos < frame.getWidth(); xPos++) {
                boolean isInExclude = false;//get if in excluded zone
                for (Shape s : exclude) {
                    int trueX = s.getTruePos().x,
                            trueY = s.getTruePos().y;
                    if (new Rectangle(trueX, trueY, s.getWidth(), s.getHeight()).contains(xPos, yPos)) {
                        isInExclude = true;
                        break;
                    }
                }
                for (Shape s : discarded) {
                    int trueX = s.getTruePos().x,
                            trueY = s.getTruePos().y;
                    if (new Rectangle(trueX, trueY, s.getWidth(), s.getHeight()).contains(xPos, yPos)) {
                        isInExclude = true;
                        break;
                    }
                }
                if (!isInExclude && frame.getRGB(xPos, yPos) == Color.white.getRGB()) {//if not and white
                    centerY[0] = xPos;
                    centerY[1] = yPos;

                    break;

                }
            }
            if (centerY[0] > -1 && centerY[1] > -1) {
                break;
            }
        }
        System.out.println("Found!");
        if (centerY[0] > -1 && centerY[1] > -1) {
            yMax = centerY[1] + 1;
            yMin = centerY[1];
            xMin = centerY[0] - 1;
            xMax = xMin + 2;
            //get all 3 other sides
            do {

                yMax++;
                xMin--;
                xMax++;

                if (y1Stop || yMax >= frame.getHeight()) {
                    yMax--;
                }
                if (x0Stop) {
                    xMin++;
                }
                if (xMin < 0) {
                    xMin = 0;
                }
                if (x1Stop || xMax >= frame.getWidth()) {
                    xMax--;
                }
                //System.out.println(xMin+" "+xMax+" "+yMin+" "+yMax+x0Stop+x1Stop+y1Stop);
                if (!y1Stop || !x0Stop || !x1Stop) {
                    int stillInTheShape = 0;
                    //System.out.println(xMin);
                    for (int x = xMin; x < xMax; x++) {
                        //System.out.println(x+" "+yMax);
                        if (frame.getRGB(x, yMax-1) == Color.white.getRGB())//AOOB
                        {
                            stillInTheShape++;
                        }
                    }
                    if (stillInTheShape == 0 || yMax >= frame.getHeight() - 1) {
                        y1Stop = true;
                    } else if (stillInTheShape != 0) {
                        y1Stop = false;
                    }
                }
                if (!x0Stop || !x1Stop || !y1Stop) {
                    int stillInTheShape = 0;
                    for (int y = yMin; y < yMax; y++) {
                        if (frame.getRGB(xMin, y) == Color.white.getRGB()) {
                            stillInTheShape++;
                        }
                    }
                    if (stillInTheShape == 0 || xMin <= 0) {
                        // System.out.println("stop at "+xMin);
                        x0Stop = true;
                    } else if (stillInTheShape != 0) {
                        x0Stop = false;
                    }
                }
                if (!x1Stop || !x0Stop || !y1Stop) {
                    //System.out.println(""+yMax+x0Stop+x1Stop+y1Stop);
                    int stillInTheShape = 0;
                    for (int y = yMin; y < yMax; y++) {
                        if (frame.getRGB(xMax-1, y) == Color.white.getRGB()) {
                            stillInTheShape++;
                        }
                    }
                    if (stillInTheShape == 0 || xMax >= frame.getWidth() - 1) {
                        //System.out.println("stop at_ "+xMax);
                        x1Stop = true;
                    } else if (stillInTheShape != 0) {
                        x1Stop = false;
                    }
                }
            } while (!x0Stop || !x1Stop || !y1Stop);
            System.out.println("shape at (" + xMin + "," + yMin + ") (" + xMax + "," + yMax + ")");
            int[][] templateVal = new int[yMax - yMin][xMax - xMin];//get the value of shape
            for (int i = yMin; i < yMax; i++) {
                for (int j = xMin; j < xMax; j++) {
                    templateVal[i - yMin][j - xMin] = frame.getRGB(j, i);
                }
            }
            Template t = new Template(templateVal);
            if (this.checkIfTemplateExists(t, ERROR_MARGIN)) {
                //write template    ---------------------------------------------------------------------------------------
            }

            Point truepos = new Point(xMin, yMin);
            Shape s = new Shape(t, truepos);
            if(s.getWidth()*s.getHeight() > frame.getWidth()*frame.getHeight()/SMALLSHAPE_FACTOR){
                detectedShapes.add(s);
            }else{
                discarded.add(s);
            }
            

            detectShape(detectedShapes,yPos);//call yourself until no more shapes
        } else {
            return true;
        }
        //System.out.println("There was a problem in PatterDetector.detectShapes: this line isnt supposed to be executed.");
        return true;
    }

    /**
     * Checks all already saved templates to see if _template matches at errorMargin % of error margin --------------------
     * scale the biggest template of the 2 to the size of the smallest before compare
     * if match, return true, else return false(and possibly write a new template accordingly)
     * @deprecated not implemented yet
     */
    @Deprecated
    public boolean checkIfTemplateExists(Template _template, int errorMargin) {
        

        return false;
    }

    /**
     * Returns all the shapes that have been found by the detector. Due to the multi-threaded nature of the process, finding all the shapes is not guarentied.
     * @return an <code>List</code> of <code>Shapes</code> that are currently found.
     */
    public List<Shape> getShapes() {
        return detectedShapes;
    }

    /**
     * Adds che given <code>Shape</code> to the current list of detected shapes
     * @param s the shape to be added
     */
    public void addToShapes(Shape s) {
        detectedShapes.add(s);
    }
    /**
     * Returns the shapes that have been discarded, bue to their size. The shapes are likely to be some sort of interference.
     * @return an <code>List</code> of <code>Shapes</code> that have been ignored
     */
    public List<Shape> getDiscarded() {
        return discarded;
    }

    /**
     * Adds the shape <code>s</code> to the list of discarded shapes.
     * @param s 
     */
    public void addToDiscarded(Shape s) {
        discarded.add(s);
    }
}
