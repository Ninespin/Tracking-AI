/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shapes;

import java.io.Serializable;

/**
 *
 * @author jeremi
 */
public class Shape{
    private int[] truePosition,center;
    private Template template;
    private int width,height;
    
    public Shape(){}
    public Shape(Template _t, int[] _truePos){
        template = _t;
        truePosition = _truePos;
        width = _t.getWidth();
        height = _t.getHeight();
        center[0] = (int)(truePosition[0]/2);
        center[1] = (int)(truePosition[1]/2);
    }
    public Shape(Template _t, int[] _truePos, int _width, int _height){
        template = _t;
        truePosition = _truePos;
        width = _width;
        height = _height;
        center[0] = (int)(truePosition[0]/2);
        center[1] = (int)(truePosition[1]/2);
    }
    
    public int[] getTruePos(){
        return truePosition;
    }
    public int[] getCenter(){
        return center;
    }
    public Template getTemplate(){
        return template;
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    
    public void setTruePos(int[] _p){
        truePosition = _p;
    }
    public void setCenter(int[] _p){
        center = _p;
    }
    public void setTemplate(Template _t){
        template = _t;
    }
    public void setWidth(int _p){
        width = _p;
    }
    public void setHeight(int _p){
        height = _p;
    }

}
