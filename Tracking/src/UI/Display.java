/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Shapes.PatternDetector;
import Shapes.Shape;
import graphics.Frame;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ConcurrentModificationException;
import javax.swing.JPanel;
import javax.swing.Scrollable;

/**
 *
 * @author jeremi
 */
public class Display extends JPanel implements Scrollable{

    private BufferedImage frame;

    private Thread th;
    private boolean running = true;
    PatternDetector p;

    public Display() {
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
        Dimension d = new Dimension(frame.getWidth(),frame.getHeight());
        this.setMinimumSize(d);
        this.setPreferredSize(d);
        invalidate();
        repaint();
    }

    public void setFrame(Frame _frame) {
        frame = _frame.getImage();
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
            g.drawImage(frame, 0, 0, frame.getWidth(), frame.getHeight(), null);//changed to true frame wt/ht

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
