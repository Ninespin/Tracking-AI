/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import IO.IImageProcessor;
import Shapes.PatternDetector;
import UI.Display;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author jeremi
 */
public class FrameProcessor implements IImageProcessor{
    
    private final Filter filter ;
    private final Display display;
    private final PatternDetector out;
    
    public FrameProcessor(Filter _filter,Display dis,PatternDetector out){
        filter = _filter;
        display = dis;
        this.out = out;
    }
    
    /**
     * returns a black and white image
     * the white portion is what matches 
     */
    //TODO make the margin thing make sense : when it is 0 it is more tolerant than when it is 100!
    public BufferedImage applyFilter(BufferedImage frame, Filter filter){
        int precision = (filter.getPrecision() < 0)? 0 : (filter.getPrecision() > 100)? 100 : filter.getPrecision();
        int[] _colorComp = {filter.getColor().getRed(),filter.getColor().getGreen(),filter.getColor().getBlue()};
        final int margin = (int)(precision*255/100);
        
        BufferedImage filteredFrame = new BufferedImage(frame.getWidth(),frame.getHeight(),BufferedImage.TYPE_INT_RGB);
        
        for(int y = 0; y < frame.getHeight();y++){
            for(int x = 0; x < frame.getWidth();x++){
                Color tc = new Color(frame.getRGB(x, y));
                int[] targetColor = {tc.getRed(),tc.getGreen(),tc.getBlue()};
                    //OLD WAY
                        /*(targetColor[0] <= ((_colorComp[0]+margin < 255)? _colorComp[0]+margin : 255) && 
                         targetColor[0] >= ((_colorComp[0]-margin > 0)? _colorComp[0]-margin : 0))&&//red
                        (targetColor[1] <= ((_colorComp[1]+margin < 255)? _colorComp[1]+margin : 255)
                        && targetColor[1] >= ((_colorComp[1]-margin > 0)? _colorComp[1]-margin : 0))&&//green    
                        (targetColor[2] <= ((_colorComp[2]+margin < 255)? _colorComp[2]+margin : 255) 
                        && targetColor[2] >= ((_colorComp[2]-margin > 0)? _colorComp[2]-margin : 0))*/      
                if(filter.getColor() == Color.red || filter.getColor() == Color.blue || filter.getColor() == Color.green){
                    if(_colorComp[0]>_colorComp[1]&&_colorComp[0]>_colorComp[2]){//most red
                        if(targetColor[0] >((targetColor[1]+margin >= 255)? 254:targetColor[1]+margin) && 
                                targetColor[0] > ((targetColor[2]+margin >= 255)? 254:targetColor[2]+margin) ){
                            filteredFrame.setRGB(x,y,Color.white.getRGB());
                        }
                    }else if(_colorComp[0]<_colorComp[1]&&_colorComp[1]>_colorComp[2]){//most green
                        if(targetColor[1] >((targetColor[0]+margin >= 255)? 254:targetColor[0]+margin) && 
                                targetColor[1] > ((targetColor[2]+margin >= 255)? 254:targetColor[2]+margin) ){
                            filteredFrame.setRGB(x,y,Color.white.getRGB());
                        }
                    }else if(_colorComp[0]<_colorComp[2]&&_colorComp[1]<_colorComp[2]){//most blue
                        if(targetColor[2] >((targetColor[0]+margin >= 255)? 254:targetColor[0]+margin) && 
                                targetColor[2] > ((targetColor[1]+margin >= 255)? 254:targetColor[1]+margin) ){
                            filteredFrame.setRGB(x,y,Color.white.getRGB());
                        }
                    }
                }else{
                    if(targetColor[0]-targetColor[1] <= _colorComp[0]-_colorComp[1]+margin && 
                            targetColor[0]-targetColor[1] >= _colorComp[0]-_colorComp[1]-margin &&
                            targetColor[1]-targetColor[2] <= _colorComp[1]-_colorComp[2]+margin && 
                            targetColor[1]-targetColor[2] >= _colorComp[1]-_colorComp[2]-margin ){
                        filteredFrame.setRGB(x,y,Color.white.getRGB());
                    }
                }
                
            }
        }
        return filteredFrame;
        
    }

    /**
     * Called by a <code>FrameStream</code>, this method recieves a frame and processes it.
     * @param frame the frame to process
     */
    @Override
    public void process(Frame frame) {
        Frame f = new Frame (applyFilter(frame.getImage(),filter));
        f.setTrueImage(frame.getImage());
        
        out.nextFrame(f);
        
        display.setFrame(f);
    }
}
