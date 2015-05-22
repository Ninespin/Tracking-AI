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
        
        for(int i = 0 ; i < height;i++){
            for(int j = 0; j < width;j++){
                template[i][j] = _image.getRGB(i,j);    ///COORDFINATES OOB
            }
        }
    }
    
    public int[][] getRawTemplate(){
        return template;
    }
    
    public BufferedImage toImage(){
        BufferedImage bu = new BufferedImage(template[0].length, template.length, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < template.length; i++) {
            for (int j = 0; j < template[0].length; j++) {
                try{
                    
                    
                    bu.setRGB(j, i, template[i][j]); //bu seems not to be the right size... out of bounds is on bu
                    
                }catch(Exception e){
                    System.out.println("bu "+bu.getWidth()+"."+template[0].length+","+bu.getHeight()+"."+template.length);
                    System.out.println("("+j+"."+i+
                        ") is out of bounds, it shouldnt have been reached infirst place.(Template.java.toImage())");
                e.printStackTrace();
                }
                
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