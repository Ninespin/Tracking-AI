/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shapes;

import java.awt.Point;


/**
 *
 * @author jeremi
 */
public class Shape{
    private Point truePosition,center;
    private Template template;
    private int width,height;
    
    public Shape(){}
    public Shape(Template _t, Point _truePos){
        template = _t;
        truePosition = _truePos;
        width = _t.getWidth();
        height = _t.getHeight();
        center.x = (int)(truePosition.x/2);
        center.y = (int)(truePosition.y/2);
    }
    public Shape(Template _t,Point _truePos, int _width, int _height){
        this(_t,_truePos);
        width = _width;
        height = _height;
    }
    
    public Point getTruePos(){
        return truePosition;
    }
    public Point getCenter(){
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
    
    public void setTruePos(Point _p){
        truePosition = _p;
    }
    public void setCenter(Point _p){
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
