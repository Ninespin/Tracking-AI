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
    
    private BufferedImage image;
    private List<Shape> shapes;

    /**
     * Crates a new frame, Containing the image <code>image</code>.
     * @param image the image representad by this frame
     */
    public Frame(BufferedImage image) {
        this.image = image;
        shapes = new LinkedList<>();
    }

    /**
     * Getter for the image of the frame
     * @return the unaltered image of this frame, as an instance of <code>BufferedImage</code>
     */
    public BufferedImage getImage() {
        return image;
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
}
