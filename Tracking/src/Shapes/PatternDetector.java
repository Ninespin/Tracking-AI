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
    
    public PatternDetector(){}
    public PatternDetector(BufferedImage _frame){
        frame = _frame;
        
    }
    
    public boolean detectShapes(ArrayList<Shape> exclude){
        int[]centerY = {-1,-1};//x,y
        int xMax,xMin,yMax,yMin;
        boolean x0Stop = false, x1Stop = false,y1Stop = false;
        
        //first encounter  ** missing -> check if point is in not exclude
        for(int y0 = 0; y0 < frame.getHeight();y0++){
            for(int x = 0; x < frame.getWidth();x++){
                boolean isInExclude = false;//get if in excluded zone
                for(Shape s : exclude){
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
            yMax = centerY[1];
            yMin = yMax;
            xMin = centerY[0];
            xMax = xMin;
            //get all 3 other sides
            do{
                yMax++;
                xMin++;
                xMax++;
                if(!y1Stop){
                    boolean stillInTheShape = true;
                    for(int x = centerY[0]-xMin; x < centerY[0]+xMax;x++){
                        if(frame.getRGB(x,yMax) == Color.white.getRGB())
                            stillInTheShape = false;
                    }   
                    if(!stillInTheShape)
                        y1Stop = true;
                }else{
                    yMax--;
                }
                if(!x0Stop){
                    boolean stillInTheShape = true;
                    for(int y = centerY[1]-yMin; y < centerY[0];y++){
                        if(frame.getRGB(xMin,y) == Color.white.getRGB())
                            stillInTheShape = false;
                    }   
                    if(!stillInTheShape)
                        x0Stop = true;
                }else{
                    xMin--;
                }
                if(!x1Stop){
                    boolean stillInTheShape = true;
                    for(int y = centerY[1]; y < centerY[0]+yMax;y++){
                        if(frame.getRGB(xMax,y) == Color.white.getRGB())
                            stillInTheShape = false;
                    }   
                    if(!stillInTheShape)
                        x1Stop = true;
                }else{
                    xMax--;
                }
            }while(!x0Stop || !x1Stop || !y1Stop);
            
            int[][] templateVal = new int[yMax-yMin][xMax-xMin];//get the value of shape
            for(int i = yMin; i < yMax;i++){
                for(int j = xMin; j < xMax;j++){
                    templateVal[i][j] = frame.getRGB(xMin+j,yMin+i);
                }
            }
            Template t = new Template(templateVal);
            if(this.checkIfTemplateExists(t,ERROR_MARGIN)){
                //write template    ---------------------------------------------------------------------------------------
            }
            
            Point truepos = new Point(xMin,xMax);
            detectedShapes.add(new Shape(t,truepos));
            
            detectShapes(detectedShapes);//call yourself until no more shapes
        }else{
            return true;
        }
        System.out.println("There was a problem in PatterDetector.detectShapes: this line isnt supposed to be executed.");
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
}
