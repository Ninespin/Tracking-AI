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
import java.util.ConcurrentModificationException;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import remote.controller.RemoteController;
import tracking.DisplacementVector;
import tracking.Tracking;

/**
 *
 * @author jeremi
 */
public class Display extends JPanel implements Scrollable {
    
    private int millisecondsPerUpdate = 1000;
    
    private Frame frame;

    private Thread th;
    private boolean running = true,paintOriginal = false,enphaciseOriginal = false,
            showMatchString = false;
    PatternDetector p;

    private Tracking t;
    
    private DisplacementVector dv;
    private RemoteController remote;

    public Display() {
        
        this.setSize(new Dimension(200, 200));
        th = new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep(millisecondsPerUpdate);
                } catch (InterruptedException ex) {
                }
                this.refresh();
            }
        }, "Display thread");
    }

    public void setMillisecondsPerUpdate(int millisecondsPerUpdate) {
        this.millisecondsPerUpdate = millisecondsPerUpdate;
    }

    public void start(Tracking t) {
        this.t= t;
        if (th.getState() == Thread.State.NEW) {
            th.start();
        }
    }

    public void refresh() {
        if(frame != null){
            Dimension d = new Dimension(frame.getImage().getWidth(), frame.getImage().getHeight());
            this.setMinimumSize(d);
            this.setPreferredSize(d);
            invalidate();
            repaint();
        }
    }

    public void setFrame(Frame _frame) {
        frame = _frame;
        if (p != null) {
            p.stop();
        }
        p = new PatternDetector(_frame);
        p.detectShapes(null);
        System.out.println("t"+t);
        t.nextFrame(frame);//this motherfucker (t) is null... why?
        //Shape match = t.getHighestMatch();
        invalidate();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        g = (Graphics2D) g;
        g.setColor(Color.black);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        if (frame != null) {
            g.drawImage((paintOriginal)? frame.getTrueImage():frame.getImage(), 
                    0, 0, frame.getImage().getWidth(), frame.getImage().getHeight(), null);//changed to true frame wt/ht
            g.setColor(new Color(0,0,0,0.75f));
            
            if(paintOriginal && enphaciseOriginal)g.fillRect( 0, 0, frame.getImage().getWidth(), frame.getImage().getHeight());

            
            /*TRACKING TEST*/
            if(t!= null){
                try {
                    if(t.getLastFrame() == null){t.nextFrame(frame);}
                    if (frame.getShapes().size() > 0) {
                        Shape match = t.getHighestMatch();

                        if (t.getFirstFrame() != null) {
                            g.setColor(Color.yellow);
                            dv = t.getDisplacementVector();
                            g.drawRect(dv.getT1().getTruePos().x, dv.getT1().getTruePos().y,
                                    dv.getT1().getWidth(), dv.getT1().getHeight());
                            g.drawLine(t.getPts()[0].x, t.getPts()[0].y, t.getPts()[1].x, t.getPts()[1].y);
                            /**SENDING COORDS**/
                            if(remote != null && dv != null){
                                double dx = dv.getDisplacement()[1].x-dv.getDisplacement()[0].x;
                                double dy = dv.getDisplacement()[1].y-dv.getDisplacement()[0].y;
                                System.out.println("dx"+dx+" dy"+dy);
                                remote.send("M:"+dx/100+":"+0);//dy/100);
                            }
                            /**END**/
                        }
                        g.setColor(Color.green);
                        if(match != null)
                            g.drawRect(match.getTruePos().x, match.getTruePos().y, match.getWidth(), match.getHeight());

                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
            /*END TRACKING TEST*/
            /**/
            try {
                for (int i = 0; i < p.getShapes().size(); i++) {
                    Shape s = p.getShapes().get(i);
                    System.out.println("p."+p.getShapes().size());
                    g.setColor(Color.red);
                    if(paintOriginal && enphaciseOriginal)g.drawImage(frame.getTrueImage().getSubimage(s.getTruePos().x, s.getTruePos().y, s.getWidth(), s.getHeight()),
                            s.getTruePos().x, s.getTruePos().y, s.getWidth(), s.getHeight(),null);
                    g.drawRect(s.getTruePos().x, s.getTruePos().y, s.getWidth(), s.getHeight());
                    g.drawLine(s.getCenter().x - s.getWidth() / 20, s.getCenter().y, s.getCenter().x + s.getWidth() / 20, s.getCenter().y);
                    g.drawLine(s.getCenter().x, s.getCenter().y - s.getHeight() / 20,
                            s.getCenter().x, s.getCenter().y + s.getHeight() / 20);
                    g.drawString("" + i, s.getTruePos().x + 5, s.getTruePos().y + 10);
                    if(t.getMatches() != null){
                        String match = (t.getMatchFor(i)*100 < 10)? "0"+(int)(t.getMatchFor(i)*100):(int)(t.getMatchFor(i)*100)+"%";
                        if(showMatchString)g.drawString(match, (int)(s.getCenter().x-g.getFontMetrics().stringWidth(match)/2),
                            s.getTruePos().y - 10);
                    }
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
    
    public void setPaintOriginal(boolean b){
        paintOriginal = b;
    }
    public void setEmphasis(boolean b){
        enphaciseOriginal = b;
    }
    public void setMatchStringVisible(boolean b){
        showMatchString = b;
    }

    private void paintString(Graphics g, String s) {
        g.drawString(s, this.getWidth() / 2 - (g.getFontMetrics().stringWidth(s)) / 2, this.getHeight() / 2 - 6);
    }

    public DisplacementVector getLastVector(){
        return dv;
    }
    public void resetLastVector(){
        dv = null;
    }
    public void passRemote(RemoteController r){
        remote = r;
    }
    
    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle rctngl, int i, int i1) {
        return this.getWidth() / 10;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle rctngl, int i, int i1) {
        return this.getWidth() / 10;
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
