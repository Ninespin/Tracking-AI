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
    
    
    public Template(){
        width = 100;
        height = 100;
        template = new int[width][height];
    }
    public Template(int[][] _t){
        template = _t;
        width= template[0].length;
        height = template.length;
    }
    
    public Template(BufferedImage _image){
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
    
    public BufferedImage toImage(){
        BufferedImage bu = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < bu.getHeight(); i++) {
            for (int j = 0; j < bu .getWidth(); j++) {
                bu.setRGB(i, j, template[i][j]);
            }
        }
        return bu;
    }
    
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
}