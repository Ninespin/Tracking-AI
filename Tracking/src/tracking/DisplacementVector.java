/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tracking;

import Shapes.Shape;
import graphics.Frame;
import java.awt.Point;

/**
 *
 * @author jeremi
 */
public class DisplacementVector {
    private final byte TYPE = 2; //3d displacement or 2d displacement
    private Shape t1,t2;
    
 
    public DisplacementVector(Frame f1,Frame f2){
        t1 = f1.getTrackedShape();
        t2 = f2.getTrackedShape();
        
    }
    
    /*
        gets the 2 points of the center of the 2 tracked shapes of frame_1 and frame_2
    */
    public Point[] getDisplacement(){
       
        Point[] pts = new Point[2];
        pts[0] = t1.getCenter();
        pts[1] = t2.getCenter();
        
        return pts;
    }
    
    /*
        returns the distance between point t1.getCenter() and t2.getCenter()
    */
    public double getDistance(){
        return Math.sqrt(Math.pow(t1.getCenter().x-t2.getCenter().x,2)+Math.pow(t1.getCenter().y-t2.getCenter().y,2));
    }
    
    public Shape getT1(){
        return t1;
    }
    public Shape getT2(){
        return t2;
    }
    
}
