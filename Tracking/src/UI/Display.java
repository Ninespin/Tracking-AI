/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author jeremi
 */
public class Display extends JPanel{
    private BufferedImage frame;
    
    public Display(){
        this.setSize(new Dimension(200,200));
        
    }
    
    public void setFrame(BufferedImage _frame){
        frame = _frame;
        repaint();
    }
    
    
    
    public void paintComponent(Graphics g){
        g = (Graphics2D) g;
        g.setColor(Color.black);
        g.drawRect(0, 0, this.getWidth(), this.getHeight());
        if(frame != null){
            g.drawImage(frame,0,0,this.getWidth(),this.getHeight(),null);
        }else{
            g.setColor(Color.yellow);
            g.drawString("There is no frame to draw", this.getWidth()/2 -6 , this.getHeight()/2 -6);
        }
        
    }
}
