/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Shapes.PatternDetector;
import Shapes.Shape;
import Shapes.Template;
import graphics.Frame;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.util.ConcurrentModificationException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import tracking.DisplacementVector;
import tracking.Tracking;

/**
 *
 * @author jeremi
 */
public class Display extends JPanel implements Scrollable{

    private Frame frame;

    private Thread th;
    private boolean running = true;
    PatternDetector p;

    private Tracking t;
    
    
    public Display() {
        try{
            Template temp = new Template(ImageIO.read(new File("C:\\Users\\jérémi\\Desktop\\template.png")));
            t = new Tracking(temp);
        }catch(Exception e){}
        
        this.setSize(new Dimension(200, 200));
        th = new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
                this.refresh();
                //System.out.println("Repaint!");
            }
        }, "Display thread");
    }

    public void start() {
        th.start();
    }

    public void refresh(){
        Dimension d = new Dimension(frame.getImage().getWidth(),frame.getImage().getHeight());
        this.setMinimumSize(d);
        this.setPreferredSize(d);
        invalidate();
        repaint();
    }

    public void setFrame(Frame _frame) {
        frame = _frame;
        if(p!= null)
            p.stop();
        p = new PatternDetector(_frame);
        p.detectShapes(null); 
        invalidate();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        g = (Graphics2D) g;
        g.setColor(Color.black);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        if (frame != null) {
            g.drawImage(frame.getImage(), 0, 0, frame.getImage().getWidth(), frame.getImage().getHeight(), null);//changed to true frame wt/ht

            /**/
            try {
                for (int i = 0; i < p.getShapes().size(); i++) {
                    Shape s = p.getShapes().get(i);
                    g.setColor(Color.red);
                    g.drawRect(s.getTruePos().x, s.getTruePos().y, s.getWidth(), s.getHeight());
                    g.drawLine(s.getCenter().x - s.getWidth() / 20, s.getCenter().y, s.getCenter().x + s.getWidth() / 20, s.getCenter().y);
                    g.drawLine(s.getCenter().x, s.getCenter().y - s.getHeight() / 20,
                            s.getCenter().x, s.getCenter().y + s.getHeight() / 20);
                    g.drawString("" + i, s.getTruePos().x + 5, s.getTruePos().y + 10);
                }
            } catch (ConcurrentModificationException e) {
                g.setColor(Color.yellow);
                paintString(g, "Wait");
            }

            /**/
            
            /*TRACKING TEST*/
            try{
                if(frame.getShapes().size()>0){
                    
                    //System.out.println("th"+temp.getHeight()+" tw"+temp.getWidth());
                    Frame bufferF = t.getFirstFrame();
                    t.nextFrame(frame);
                    
                    Shape match = t.getHighestMatch();
                    
                    if(t.getFirstFrame() != null){
                        g.setColor(Color.yellow);
                        DisplacementVector d = t.getDisplacementVector();
                        g.drawRect(d.getT1().getTruePos().x, d.getT1().getTruePos().y,
                                d.getT1().getWidth(),d.getT1().getHeight());
                        g.drawLine(t.getPts()[0].x,t.getPts()[0].y,t.getPts()[1].x,t.getPts()[1].y);
                    }
                    g.setColor(Color.green);
                    g.drawRect(match.getTruePos().x, match.getTruePos().y, match.getWidth(), match.getHeight());
                    
                }
            }catch(Exception e){
                System.out.println("C:\\Users\\jérémi\\Desktop\\template.png was probably not found");
                e.printStackTrace();
            }
            
            /*END TRACKING TEST*/
        } else {
            g.setColor(Color.yellow);
            g.drawString("There is no frame to draw", this.getWidth() / 2 - (g.getFontMetrics().stringWidth("There is no frame to draw")) / 2, this.getHeight() / 2 - 6);
        }

    }

    private void paintString(Graphics g, String s) {
        g.drawString(s, this.getWidth() / 2 - (g.getFontMetrics().stringWidth(s)) / 2, this.getHeight() / 2 - 6);
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle rctngl, int i, int i1) {
        return this.getWidth()/10;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle rctngl, int i, int i1) {
        return this.getWidth()/10;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}
