/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shapes;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author jeremi
 */
public class PatternDetector {
    private ArrayList<Shape> detectedShapes;
    private BufferedImage frame;
    
    
    public PatternDetector(){}
    public PatternDetector(BufferedImage _frame){
        frame = _frame;
        
    }
    
    public void detectShapes(){
        
    }
    
    public boolean checkIfTemplateExists(Template _template, int errorMargin){
        /*
            checks all already saved templates to see if _template matches at errorMargin % of error margin
                scale the biggest template of the 2 to the size of the smallest before compare
            if match, return true, else return false(and possibly write a new template accordingly)
        */
        
        return false;
    }
    
    private Shape makeShape(int x0,int y0,int x1, int y1){
        Template template = new Template();
        Shape shape = new Shape();
        
        
        return shape;
    }
}
