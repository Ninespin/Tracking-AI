/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import IO.IImageProcessor;
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
    
    public FrameProcessor(Filter _filter,Display dis){
        filter = _filter;
        display = dis;
    }
    
    /*
    returns a black and white image
    the white portion is what matches 
    */
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
                
            }
        }
        return filteredFrame;
        
    }

    @Override
    public void process(Frame frame) {
        display.setFrame(new Frame (applyFilter(frame.getImage(),filter)));
    }
}
