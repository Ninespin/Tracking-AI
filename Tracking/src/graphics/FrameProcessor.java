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
        int precision = (filter.getUncertainty() < 0)? 0 : (filter.getUncertainty() > 100)? 100 : filter.getUncertainty();
        int[] _colorComp = {filter.getColor().getRed(),filter.getColor().getGreen(),filter.getColor().getBlue()};
        final int margin = (int)(precision*255/100);
        
        BufferedImage filteredFrame = new BufferedImage(frame.getWidth(),frame.getHeight(),BufferedImage.TYPE_INT_RGB);
        
        for(int y = 0; y < frame.getHeight();y++){
            for(int x = 0; x < frame.getWidth();x++){
                Color tc = new Color(frame.getRGB(x, y));
                int[] targetColor = {tc.getRed(),tc.getGreen(),tc.getBlue()};
                
                //filteredImage.setRGB(x,y,tc.getRed()<<16);
                
                if((targetColor[0] <= ((_colorComp[0]+margin < 255)? _colorComp[0]+margin : 255) && 
                         targetColor[0] >= ((_colorComp[0]-margin > 0)? _colorComp[0]-margin : 0))&&//red
                        (targetColor[1] <= ((_colorComp[1]+margin < 255)? _colorComp[1]+margin : 255)
                        && targetColor[1] >= ((_colorComp[1]-margin > 0)? _colorComp[1]-margin : 0))&&//green    
                        (targetColor[2] <= ((_colorComp[2]+margin < 255)? _colorComp[2]+margin : 255) 
                        && targetColor[2] >= ((_colorComp[2]-margin > 0)? _colorComp[2]-margin : 0))){//blue
                    filteredFrame.setRGB(x,y,Color.white.getRGB());
                }/*
                if(_colorComp[0] > _colorComp[1] && _colorComp[0] > _colorComp[2]){//all where red is higher
                    if(targetColor[0] > targetColor[1] && targetColor[0] > targetColor[2]){
                        filteredImage.setRGB(x,y,_highlightColor.getRGB());
                    }
                }else if(_colorComp[2] > _colorComp[1] && _colorComp[0] < _colorComp[2]){//all where blue is higher
                    if(targetColor[2] > targetColor[1] && targetColor[0] < targetColor[2]){
                        filteredImage.setRGB(x,y,_highlightColor.getRGB());
                    }
                }
                else if(_colorComp[0] < _colorComp[1] && _colorComp[1] > _colorComp[2]){//all where green is higher
                    if(targetColor[0] < targetColor[1] && targetColor[1] > targetColor[2]){
                        filteredImage.setRGB(x,y,_highlightColor.getRGB());
                    }
                }*/
            }
        }
        return filteredFrame;
        
    }

    @Override
    public void process(Frame frame) {
        display.setFrame(applyFilter(frame.getImage(),filter));
    }
}
