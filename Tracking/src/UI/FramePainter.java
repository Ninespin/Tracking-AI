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
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ConcurrentModificationException;
import tracking.DisplacementVector;

/**
 * An extention on graphics that can paint a shape!
 *
 * @author Aranud Paré-Vogt
 */
public class FramePainter {

    private static final int FONT_SIZE_DEFAULT = 11;

    private Color trackedShapeColor = Color.green;
    private Color genericShapeColor = Color.red;
    private Color emphasisColor = new Color(0, 0, 0, 0.75f);
    private Color displacementVectorColor = Color.YELLOW;

    private int width;
    private int height;

    private boolean paintBagkgroundImage;
    private boolean paintEmphasizedShapes;
    private boolean showMatchPercentage;
    private boolean autoResizeImage;

    public FramePainter(int width, int height, boolean paintBagkgroundImage, boolean paintEmphasizedImage, boolean showMatchPercentage, boolean autoResizeImage) {
        this.width = width;
        this.height = height;
        this.paintBagkgroundImage = paintBagkgroundImage;
        this.paintEmphasizedShapes = paintEmphasizedImage;
        this.showMatchPercentage = showMatchPercentage;
        this.autoResizeImage = autoResizeImage;
    }

    public BufferedImage paintFrame(Frame f, DisplacementVector dv) {
        int w = autoResizeImage ? width : f.getImage().getWidth();
        int h = autoResizeImage ? height : f.getImage().getHeight();
        double scaleW = (w / (double) f.getImage().getWidth());
        double scaleH = (h / (double) f.getImage().getHeight());

        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();

        paintBackground(g, f, w, h);
        paintFrameShapes(g, f, scaleW, scaleH);
        paintDisplacementVector(g, dv, scaleW, scaleH);
        
        g.dispose();
        
        return img;
    }

    private void paintBackground(Graphics2D g, Frame f, int width, int height) {
        BufferedImage bgImage = paintBagkgroundImage ? f.getTrueImage() : f.getImage();
        g.drawImage(bgImage, 0, 0, width, height, null);

        if (paintBagkgroundImage && paintEmphasizedShapes) {
            g.setColor(emphasisColor);
            g.fillRect(0, 0, width, height);
        }
    }

    private void paintDisplacementVector(Graphics2D g, DisplacementVector dv, double scaleW, double scaleH) {
        if (dv != null) {
            g.setColor(displacementVectorColor);
            g.drawRect((int) (dv.getT1().getTruePos().x * scaleW),
                    (int) (dv.getT1().getTruePos().y * scaleH),
                    (int) (dv.getT1().getWidth() * scaleW),
                    (int) (dv.getT1().getHeight() * scaleH));
            g.drawLine((int) (dv.getDisplacement()[0].x * scaleW),
                    (int) (dv.getDisplacement()[0].y * scaleH),
                    (int) (dv.getDisplacement()[1].x * scaleW),
                    (int) (dv.getDisplacement()[1].y * scaleH));
        }
    }

    private void paintFrameShapes(Graphics2D g, Frame f, double scaleW, double scaleH) {
        if (autoResizeImage) {
            int fontSize = (int) (FONT_SIZE_DEFAULT * scaleW);
            g.setFont(new Font("Tahoma", Font.PLAIN, Math.max(9, fontSize)));
        }
        try {
            g.setColor(genericShapeColor);
            for (int i = 0; i < f.getShapes().size(); i++) {
                Shape s = f.getShapes().get(i);

                if (paintBagkgroundImage && paintEmphasizedShapes) {
                    g.drawImage(f.getTrueImage().getSubimage(s.getTruePos().x, s.getTruePos().y, s.getWidth(), s.getHeight()),
                            (int) (s.getTruePos().x * scaleW),
                            (int) (s.getTruePos().y * scaleH),
                            (int) (s.getWidth() * scaleW),
                            (int) (s.getHeight() * scaleH),
                            null);
                }
                g.drawRect((int) (s.getTruePos().x * scaleW),
                        (int) (s.getTruePos().y * scaleH),
                        (int) (s.getWidth() * scaleW),
                        (int) (s.getHeight() * scaleH));
                g.drawLine((int) ((s.getCenter().x - s.getWidth() / 20) * scaleW), (int) ((s.getCenter().y) * scaleH), (int) ((s.getCenter().x + s.getWidth() / 20) * scaleW), (int) (s.getCenter().y * scaleH));
                g.drawLine((int) (s.getCenter().x * scaleW),
                        (int) ((s.getCenter().y - s.getHeight() / 20) * scaleH),
                        (int) (s.getCenter().x * scaleW),
                        (int) ((s.getCenter().y + s.getHeight() / 20) * scaleH));
                g.drawString("" + i,
                        (int) ((s.getTruePos().x) * scaleW) + g.getFontMetrics().charWidth(' '),
                        (int) (s.getTruePos().y * scaleH) + g.getFontMetrics().getHeight());
                if (f.isValidMatches()) {
                    String match = (f.getShapeMatch(i) * 100 < 10) ? "0" + (int) (f.getShapeMatch(i) * 100) : (int) (f.getShapeMatch(i) * 100) + "%";
                    if (showMatchPercentage) {
                        g.drawString(match,
                                (int) (s.getCenter().x * scaleW) - g.getFontMetrics().stringWidth(match) / 2,
                                (int) (s.getTruePos().y * scaleH) - g.getFontMetrics().getHeight());
                    }
                }
                //System.out.println(g.getFont());
            }
        } catch (ConcurrentModificationException e) {
            //wait
        }

        if (f.getShapes().size() > 0) {
            Shape trackedShape = f.getTrackedShape();
            g.setColor(trackedShapeColor);
            if (trackedShape != null) {
                g.drawRect((int) (trackedShape.getTruePos().x * scaleW),
                        (int) (trackedShape.getTruePos().y * scaleH),
                        (int) (trackedShape.getWidth() * scaleW),
                        (int) (trackedShape.getHeight() * scaleH));
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

}
