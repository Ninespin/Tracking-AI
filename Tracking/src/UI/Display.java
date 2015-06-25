/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import graphics.Frame;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
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
    private boolean running = true, paintOriginal = false, enphaciseOriginal = false,
            showMatchString = false, autoResizeImage = false;

    private Tracking t;

    private DisplacementVector dv;
    private RemoteController remote;
    
    private FramePainter painter;

    public Display() {
        this.setSize(new Dimension(400, 400));
        painter = new FramePainter(this.getWidth(), this.getHeight(), paintOriginal, enphaciseOriginal, showMatchString,autoResizeImage);
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
        this.t = t;
        if (th.getState() == Thread.State.NEW) {
            th.start();
        }
    }

    public void refresh() {
        if (frame != null) {
            Dimension d = new Dimension(frame.getImage().getWidth(), frame.getImage().getHeight());
            this.setMinimumSize(d);
            this.setPreferredSize(d);
            invalidate();
            repaint();
        }
    }

    public void setFrame(Frame _frame) {
        frame = _frame;
        invalidate();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        g = (Graphics2D) g;
        g.setColor(Color.black);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        painter.setGraphics(g);
        if(frame != null){
            painter.paintFrame(frame, t!=null ? t.getDisplacementVector() : null);
        }else {
            g.setColor(Color.yellow);
            g.drawString("There is no frame to draw", this.getWidth() / 2 - (g.getFontMetrics().stringWidth("There is no frame to draw")) / 2, this.getHeight() / 2 - 6);
        }

    }

    public void setPaintOriginal(boolean b) {
        paintOriginal = b;
        painter.setPaintBagkgroundImage(paintOriginal);
    }

    public void setEmphasis(boolean b) {
        enphaciseOriginal = b;
        painter.setPaintEmphasizedShapes(enphaciseOriginal);
    }

    public void setMatchStringVisible(boolean b) {
        showMatchString = b;
        painter.setShowMatchPercentage(showMatchString);
    }

    public void setAutoResizeImage(boolean autoResizeImage) {
        this.autoResizeImage = autoResizeImage;
        painter.setAutoResizeImage(autoResizeImage);
    }

    public DisplacementVector getLastVector() {
        return dv;
    }

    public void resetLastVector() {
        dv = null;
    }

    public void passRemote(RemoteController r) {
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
