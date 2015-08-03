/*
 * The MIT License
 *
 * Copyright 2015 Eloi.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package UI.fx;

import UI.FramePainter;
import UI.IDisplayer;
import graphics.Frame;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import tracking.DisplacementVector;
import tracking.Tracking;

/**
 * Replaces Display for use with javafx
 *
 * @author Arnaud Paré-Vogt & Jérémi Cyr
 */
public class FXDisplay implements IDisplayer {

    private ScrollPane parent;

    private ImageView display;
    private FramePainter painter;

    private int millisecondsPerUpdate = 1000;

    private Frame frame;
    private DisplacementVector dv;

    private Thread th;
    private boolean running = true, paintOriginal = false, enphaciseOriginal = false,
            showMatchString = false, autoResizeImage = false;

    private Tracking t;

    private BufferedImage rawImage;
    private Image lastImage;

    public FXDisplay(ImageView display, ScrollPane parent) {
        this.display = display;
        this.parent = parent;
        painter = new FramePainter((int) display.getFitWidth(), (int) display.getFitHeight(), paintOriginal, enphaciseOriginal, showMatchString, autoResizeImage);
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
            display.setFitHeight(frame.getImage().getHeight());
            display.setFitWidth(frame.getImage().getWidth());
            if (autoResizeImage) {
                int width = (int) (parent.getHeight());
                int height = (int) (parent.getWidth());
                display.setFitHeight(height);
                display.setFitWidth(width);
                painter.setHeight(height);
                painter.setWidth(width);
            }
            repaint();
        }
    }

    @Override
    public void setFrame(Frame _frame) {
        frame = _frame;
        refresh();
    }

    private void setDimensions(int width, int height) {
        display.setFitHeight(height);
        display.setFitWidth(width);
        painter.setHeight(height);
        painter.setWidth(width);
    }

    public void repaint() {
        if (frame != null) {
            if (autoResizeImage) {
                int height = (int) (parent.getHeight());
                int width = (int) (parent.getWidth());
                display.setPreserveRatio(false);
                setDimensions(width,height);
            } else {
                int height = (int) (frame.getImage().getHeight());
                int width = (int) (frame.getImage().getWidth());
                display.setPreserveRatio(true);
                setDimensions(width,height);
            }
            if (frame.doesItNeedsRepaint()) {
                rawImage = painter.paintFrame(frame, t != null ? t.getDisplacementVector() : null);
                frame.repaint();
            } else {
                return;//we do not need repaint now!
            }
        } else {
            rawImage = new BufferedImage(frame.getImage().getWidth(), frame.getImage().getWidth(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = rawImage.createGraphics();

            g.setColor(Color.black);
            g.fillRect(0, 0, rawImage.getWidth(), rawImage.getHeight());
            g.setColor(Color.yellow);
            g.drawString("There is no frame to draw", rawImage.getWidth() / 2 - (g.getFontMetrics().stringWidth("There is no frame to draw")) / 2, rawImage.getHeight() / 2 - 6);
        }
        lastImage = SwingFXUtils.toFXImage(rawImage, null);
        Platform.runLater(() -> {
            display.setImage(lastImage);
        });
    }

    public void setPaintOriginal(boolean b) {
        paintOriginal = b;
        painter.setPaintBagkgroundImage(paintOriginal);
        if (frame != null) {
            frame.hasChangedSinceLastRepaint();
        }
    }

    public void setEmphasis(boolean b) {
        enphaciseOriginal = b;
        painter.setPaintEmphasizedShapes(enphaciseOriginal);
        if (frame != null) {
            frame.hasChangedSinceLastRepaint();
        }
    }

    public void setMatchStringVisible(boolean b) {
        showMatchString = b;
        painter.setShowMatchPercentage(showMatchString);
        if (frame != null) {
            frame.hasChangedSinceLastRepaint();
        }
    }

    public void setAutoResizeImage(boolean autoResizeImage) {
        this.autoResizeImage = autoResizeImage;
        painter.setAutoResizeImage(autoResizeImage);
        if (frame != null) {
            frame.hasChangedSinceLastRepaint();
        }
    }

    public DisplacementVector getLastVector() {
        return dv;
    }

    public void resetLastVector() {
        dv = null;
    }

    public void setTracking(Tracking t) {
        this.t = t;
    }

}
