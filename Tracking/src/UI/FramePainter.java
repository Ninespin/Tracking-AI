/*
 * The MIT License
 *
 * Copyright 2015 Aranud Paré-Vogt.
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
package UI;

import Shapes.Shape;
import graphics.Frame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ConcurrentModificationException;
import tracking.DisplacementVector;

/**
 * An extention on graphics that can paint a shape!
 *
 * @author Aranud Paré-Vogt
 */
public class FramePainter {

    private Color trackedShapeColor = Color.green;
    private Color genericShapeColor = Color.red;
    private Color emphasisColor = new Color(0, 0, 0, 0.75f);
    private Color displacementVectorColor = Color.YELLOW;

    private Graphics g;

    private int width;
    private int height;

    private boolean paintBagkgroundImage;
    private boolean paintEmphasizedShapes;
    private boolean showMatchPercentage;
    private boolean autoResizeImage;

    public FramePainter(int width, int height, boolean paintBagkgroundImage, boolean paintEmphasizedImage, boolean showMatchPercentage) {
        this.width = width;
        this.height = height;
        this.paintBagkgroundImage = paintBagkgroundImage;
        this.paintEmphasizedShapes = paintEmphasizedImage;
        this.showMatchPercentage = showMatchPercentage;
    }

    public void paintFrame(Frame f, DisplacementVector dv) {
        if (g != null) {
            paintBackground(f);
            paintDisplacementVector(dv);
            paintFrameShapes(f);
        }
    }

    private void paintBackground(Frame f) {
        BufferedImage bgImage = paintBagkgroundImage ? f.getTrueImage() : f.getImage();
        g.drawImage(bgImage, 0, 0, bgImage.getWidth(), bgImage.getHeight(), null);

        if (paintBagkgroundImage && paintEmphasizedShapes) {
            g.setColor(emphasisColor);
            g.fillRect(0, 0, bgImage.getWidth(), bgImage.getHeight());
        }
    }

    private void paintDisplacementVector(DisplacementVector dv) {
        if (dv != null) {
            g.setColor(displacementVectorColor);
            g.drawRect(dv.getT1().getTruePos().x,
                    dv.getT1().getTruePos().y,
                    dv.getT1().getWidth(),
                    dv.getT1().getHeight());
            g.drawLine(dv.getDisplacement()[0].x,
                    dv.getDisplacement()[0].y,
                    dv.getDisplacement()[1].x,
                    dv.getDisplacement()[1].y);
        }
    }

    private void paintFrameShapes(Frame f) {
        try {
            g.setColor(genericShapeColor);
            for (int i = 0; i < f.getShapes().size(); i++) {
                Shape s = f.getShapes().get(i);
                
                if (paintBagkgroundImage && paintEmphasizedShapes) {
                    g.drawImage(f.getTrueImage().getSubimage(s.getTruePos().x, s.getTruePos().y, s.getWidth(), s.getHeight()),
                            s.getTruePos().x, s.getTruePos().y, s.getWidth(), s.getHeight(), null);
                }
                g.drawRect(s.getTruePos().x, s.getTruePos().y, s.getWidth(), s.getHeight());
                g.drawLine(s.getCenter().x - s.getWidth() / 20, s.getCenter().y, s.getCenter().x + s.getWidth() / 20, s.getCenter().y);
                g.drawLine(s.getCenter().x, s.getCenter().y - s.getHeight() / 20,
                        s.getCenter().x, s.getCenter().y + s.getHeight() / 20);
                g.drawString("" + i, s.getTruePos().x + 5, s.getTruePos().y + 10);
                if (f.isValidMatches()) {
                    String match = (f.getShapeMatch(i) * 100 < 10) ? "0" + (int) (f.getShapeMatch(i) * 100) : (int) (f.getShapeMatch(i) * 100) + "%";
                    if (showMatchPercentage) {
                        g.drawString(match, (int) (s.getCenter().x - g.getFontMetrics().stringWidth(match) / 2),
                                s.getTruePos().y - 10);
                    }
                }
            }
        } catch (ConcurrentModificationException e) {
            //wait
        }

        if (f.getShapes().size() > 0) {
            Shape trackedShape = f.getTrackedShape();
            g.setColor(trackedShapeColor);
            if (trackedShape != null) {
                g.drawRect(trackedShape.getTruePos().x,
                        trackedShape.getTruePos().y,
                        trackedShape.getWidth(),
                        trackedShape.getHeight());
            }
        }
    }

    public void setAutoResizeImage(boolean autoResizeImage) {
        this.autoResizeImage = autoResizeImage;
    }

    public void setShowMatchPercentage(boolean showMatchPercentage) {
        this.showMatchPercentage = showMatchPercentage;
    }

    public void setPaintEmphasizedShapes(boolean paintEmphasizedImage) {
        this.paintEmphasizedShapes = paintEmphasizedImage;
    }

    public void setPaintBagkgroundImage(boolean paintBagkgroundImage) {
        this.paintBagkgroundImage = paintBagkgroundImage;
    }

    public void setGraphics(Graphics g) {
        if (g != null) {
            this.g = g;
        }
    }

}
