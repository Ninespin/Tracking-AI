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
 * Class that is used to pack a <code>BufferedImage</code> and the detection
 * information about it.
 *
 * @author Jérémi Cyr & Arnaud Paré-Vogt
 */
public class Frame {

    private BufferedImage image, trueImage;
    private List<Shape> shapes;
    private int trackedShapeIndex;

    private boolean validMatches;
    
    private boolean hasChangedSinceLastRepaint;

    /**
     * Crates a new frame, Containing the image <code>image</code>.
     *
     * @param image the image representad by this frame
     */
    public Frame(BufferedImage image) {
        this.image = image;
        shapes = new LinkedList<>();
        trackedShapeIndex = -1;
        validMatches = false;
        hasChangedSinceLastRepaint = true;
    }

    /**
     * Getter for the image of the frame
     *
     * @return the unaltered image of this frame, as an instance of
     * <code>BufferedImage</code>
     */
    public BufferedImage getImage() {
        return image;
    }
    /*
     trueImage is the original frame, with color
     these are the set and get methods for trueImage
     */

    public BufferedImage getTrueImage() {
        return trueImage;
    }

    public void setTrueImage(BufferedImage i) {
        hasChangedSinceLastRepaint = true;
        trueImage = i;
    }

    /**
     * Adds a new <code>Shape</code> to the currently detected shapes of this
     * frame
     *
     * @param shape the <code>Shape</code> to be added
     */
    public void addShapes(Shape shape) {
        shapes.add(shape);
        hasChangedSinceLastRepaint = true;
    }

    /**
     * Getter for the list of chapes that were detected on this frame. Because
     * the frame itself does not detect the shapes, the result depends completly
     * on the manipulations of the frame.
     *
     * @return a <code>List</code> of shapes linked to this frame.
     */
    public List<Shape> getShapes() {
        return shapes;
    }

    /**
     * sets the tracked shapes index in shapes to "i"s' value
     *
     * @param i the new Index to set
     */
    public void setTrackedShapeIndex(int i) {
        trackedShapeIndex = i;
        hasChangedSinceLastRepaint = true;
    }

    /**
     * @return trackedshapeindex
     */
    public int getTrackedShapeIndex() {
        return trackedShapeIndex;
    }

    /**
     * Returns the current shape match on a shape. Note that the shape match is
     * not guarenteed to be final, or even to have a meaning soly by calling
     * this method, you will have to look at result of
     * <code>isValidMatches()</code>.
     *
     * @param shapeIndex the shape index
     * @return the curent match of the shape, or -1 if the shape doesn't exist
     */
    public double getShapeMatch(int shapeIndex) {
        if (shapeIndex >= 0 && shapeIndex < shapes.size()) {
            return shapes.get(shapeIndex).getMatch();
        }
        return -1;
    }

    /**
     * Sets the match percentage of a shape.
     *
     * @param shapeIndex the index of the shape
     * @param shapeMatch the percentage match of the chape
     */
    public void setShapeMatch(int shapeIndex, double shapeMatch) {
        if (shapeIndex >= 0 && shapeIndex < shapes.size()) {
            shapes.get(shapeIndex).setMatch(shapeMatch);
        }
        hasChangedSinceLastRepaint = true;
    }

    public void setValidMatches(boolean validMatches) {
        this.validMatches = validMatches;
        hasChangedSinceLastRepaint = true;
    }

    public boolean isValidMatches() {
        return validMatches;
    }

    /*
     returns the tracked shape if applicable, or null and a warning
     */
    public Shape getTrackedShape() {
        if (shapes.size() > 0 && trackedShapeIndex >= 0) {
            return shapes.get(trackedShapeIndex);
        }
        return null;
    }

    /**
     * called to set the flag <code>hasChangedSinceLastRepaint</code> to false
     */
    public void repaint() {
        this.hasChangedSinceLastRepaint = false;
    }
    
    /**
     * Says if the frame needs to be repainted.
     * @return if the frame needs to be repainted
     */
    public boolean doesItNeedsRepaint(){
        return hasChangedSinceLastRepaint;
    }

    /**
     * Tells the frame that it needs to be repainted.
     */
    public void hasChangedSinceLastRepaint() {
        this.hasChangedSinceLastRepaint = true;
    }
}
