/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import Shapes.Shape;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

/**
 * Class that is used to pack a <code>BufferedImage</code> and the detection information about it.
 * @author Jérémi Cyr & Arnaud Paré-Vogt
 */
public class Frame {
    
    private BufferedImage image,trueImage;
    private List<Shape> shapes;
    private int trackedShapeIndex;

    /**
     * Crates a new frame, Containing the image <code>image</code>.
     * @param image the image representad by this frame
     */
    public Frame(BufferedImage image) {
        this.image = image;
        shapes = new LinkedList<>();
        trackedShapeIndex = -1;
    }

    /**
     * Getter for the image of the frame
     * @return the unaltered image of this frame, as an instance of <code>BufferedImage</code>
     */
    public BufferedImage getImage() {
        return image;
    }
    /*
        trueImage is the original frame, with color
        these are the set and get methods for trueImage
    */
    public BufferedImage getTrueImage(){
        return trueImage;
    }
    public void setTrueImage(BufferedImage i){
        trueImage = i;
    }
    /**
     * Adds a new <code>Shape</code> to the currently detected shapes of this frame
     * @param shape the <code>Shape</code> to be added
     */
    public void addShapes(Shape shape){
        shapes.add(shape);
    }
    
    /**
     * Getter for the list of chapes that were detected on this frame. Because the frame itself does not detect the shapes, the result depends completly on the manipulations of the frame.
     * @return a <code>List</code> of shapes linked to this frame.
     */
    public List<Shape> getShapes(){
        return shapes;
    }
    
    /*
        sets the tracked shapes index in shapes to "i"s' value
    */
    public void setTrackedShapeIndex(int i){
        trackedShapeIndex = i;
    }
    /*
        returns trackedshapeindex
    */
    public int getTrackedShapeIndex(){
        return trackedShapeIndex;
    }
    
    /*
        returns the tracked shape if applicable, or null and a warning
    */
    public Shape getTrackedShape(){
        if(shapes.size() > 0 && trackedShapeIndex >= 0){
            return shapes.get(trackedShapeIndex);
        }
        System.out.println("[WARNING] Frame.getTrackedShape returned null, shapes.size() was "+shapes.size()+" and trackedShapesIndex was "
            +trackedShapeIndex);
        return null;
    }
}
