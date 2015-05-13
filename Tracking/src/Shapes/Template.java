/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shapes;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 *
 * @author jeremi
 */
public class Template  implements Serializable{
    private int[][] template;
    private int width,height;
    
    
    public Template(){}
    public Template(int[][] _t){
        template = _t;
    }
    
    public void makeFromImage(BufferedImage _image){
        width = _image.getWidth();
        height = _image.getHeight();
        template = new int[height][width];
        
        for(int i = 0 ; i < _image.getHeight();i++){
            for(int j = 0; j < _image.getWidth();j++){
                template[i][j] = _image.getRGB(i,j);
            }
        }
    }
    
    public int[][] getRawTemplate(){
        return template;
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
}