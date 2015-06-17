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
    private double match;
    
    public Shape(){
        match = 0.0;
    }
    public Shape(Template _t, Point _truePos){
        this();
        template = _t;
        truePosition = _truePos;
        width = _t.getWidth();
        height = _t.getHeight();
        center = new Point((int)(truePosition.getX()+width/2),(int)(truePosition.getY()+height/2));
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
    public double getMatch() {
        return match;
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
    public void setMatch(double match) {
        this.match = match;
    }

}
