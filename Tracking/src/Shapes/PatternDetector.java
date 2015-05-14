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

/**
 *
 * @author jeremi
 */
public class PatternDetector {
    private ArrayList<Shape> detectedShapes;
    private BufferedImage frame;
    private final int ERROR_MARGIN = 25;//in %
    private final int SMALLSHAPE_FACTOR = 100000;
    private ArrayList<Shape> discarded;

    public PatternDetector(){}
    public PatternDetector(BufferedImage _frame){

        frame = _frame;
        detectedShapes = new ArrayList<Shape>();
        
    }
    
    public boolean detectShapes(ArrayList<Shape> exclude){
        if(exclude == null){
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
        for(int y0 = 0; y0 < frame.getHeight();y0++){
            for(int x = 0; x < frame.getWidth();x++){
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
                    if(new Rectangle(trueX,trueY,s.getWidth(),s.getHeight()).contains(x,y0)){
                        isInExclude = true;
                        break;
                    }
                }
                if(!isInExclude && frame.getRGB(x,y0) == Color.white.getRGB()){//if not and white
                    centerY[0] = x;
                    centerY[1] = y0;
                    
                    break;
                    
                }
            }
            if(centerY[0] > -1 && centerY[1] > -1){
                break;
            }
        }
        if(centerY[0] > -1 && centerY[1] > -1){
            yMax = centerY[1]+1;
            yMin = centerY[1];
            xMin = centerY[0]-1;
            xMax = xMin+2;
            //get all 3 other sides
            do{
                
                yMax++;
                xMin--;
                xMax++;
                
                if(y1Stop || yMax >= frame.getHeight()){yMax--;}
                if(x0Stop){xMin++;}
                if(xMin < 0){xMin = 0;}
                if(x1Stop || xMax >= frame.getWidth()){xMax--;}
                //System.out.println(xMin+" "+xMax+" "+yMin+" "+yMax+x0Stop+x1Stop+y1Stop);
                if(!y1Stop||!x0Stop||!x1Stop){
                    int stillInTheShape = 0;
                    //System.out.println(xMin);
                    for(int x = xMin; x < xMax;x++){
                        //System.out.println(x+" "+yMax);
                        if(frame.getRGB(x,yMax) == Color.white.getRGB())//AOOB
                            stillInTheShape++;
                    }   
                    if(stillInTheShape == 0 || yMax >= frame.getHeight()-1){
                        y1Stop = true;
                    }else if(stillInTheShape != 0){
                        y1Stop = false;
                    }
                }
                if(!x0Stop||!x1Stop||!y1Stop){
                    int stillInTheShape = 0;
                    for(int y = yMin; y < yMax;y++){
                        if(frame.getRGB(xMin,y) == Color.white.getRGB())
                            stillInTheShape++;
                    }   
                    if(stillInTheShape == 0 || xMin <= 0){
                       // System.out.println("stop at "+xMin);
                        x0Stop = true;
                    }else if(stillInTheShape != 0){
                        x0Stop = false;
                    }
                }
                if(!x1Stop||!x0Stop||!y1Stop){
                    //System.out.println(yMin+" "+xMax);
                    int stillInTheShape = 0;
                    for(int y = yMin; y < yMax;y++){
                        if(frame.getRGB(xMax,y) == Color.white.getRGB())
                            stillInTheShape++;
                    }   
                    if(stillInTheShape == 0 || xMax >= frame.getWidth()-1){
                        //System.out.println("stop at_ "+xMax);
                        x1Stop = true;
                    }else if(stillInTheShape != 0){
                        x1Stop = false;
                    }
                }
            }while(!x0Stop || !x1Stop || !y1Stop);
            //System.out.println("shape at ("+xMin+","+yMin+") ("+xMax+","+yMax+")");
            int[][] templateVal = new int[yMax-yMin][xMax-xMin];//get the value of shape
            for(int i = yMin; i < yMax;i++){
                for(int j = xMin; j < xMax;j++){
                    templateVal[i-yMin][j-xMin] = frame.getRGB(j,i);
                }
            }
            Template t = new Template(templateVal);
            if(this.checkIfTemplateExists(t,ERROR_MARGIN)){
                //write template    ---------------------------------------------------------------------------------------
            }


            Point truepos = new Point(xMin, yMin);
            
            Shape s = new Shape(t, truepos);
            if(s.getWidth()*s.getHeight() > frame.getHeight()*frame.getWidth()/SMALLSHAPE_FACTOR){
                detectedShapes.add(s);
            }else{
                discarded.add(s);
            }
            detectShapes(detectedShapes);//call yourself until no more shapes
        } else {

            return true;
        }
        //System.out.println("There was a problem in PatterDetector.detectShapes: this line isnt supposed to be executed.");
        return true;
    }
    
    public boolean checkIfTemplateExists(Template _template, int errorMargin){
        /*
            checks all already saved templates to see if _template matches at errorMargin % of error margin --------------------
                scale the biggest template of the 2 to the size of the smallest before compare
            if match, return true, else return false(and possibly write a new template accordingly)
        */
        
        return false;
    }
    
    public ArrayList<Shape> getShapes(){
        return detectedShapes;
    }
    public void addToShapes(Shape s){
        detectedShapes.add(s);
    }
    public ArrayList<Shape> getDiscarded() {
        return discarded;
    }

    public void addToDiscarded(Shape s) {
        discarded.add(s);
    }
}
