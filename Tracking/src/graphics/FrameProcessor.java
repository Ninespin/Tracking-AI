/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author jeremi
 */
public class FrameProcessor{
    private final Filter filter ;
    private final BufferedImage frame;
    
    public FrameProcessor(BufferedImage _frame,Filter _filter){
        filter = _filter;
        this.frame = _frame;
    }
    
    public void applyFilter(Color _color, double precision,Color _highlightColor){
        precision = (precision < 0)? 0 : (precision > 100)? 100 : precision;
        int[] _colorComp = {_color.getRed(),_color.getGreen(),_color.getBlue()};
        final int margin = (int)(precision*255/100);
        
        /*filteredImage = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_RGB);
        
        for(int y = 0; y < image.getHeight();y++){
            for(int x = 0; x < image.getWidth();x++){
                Color tc = new Color(image.getRGB(x, y));
                int[] targetColor = {tc.getRed(),tc.getGreen(),tc.getBlue()};
                
                //filteredImage.setRGB(x,y,tc.getRed()<<16);
                
                if((targetColor[0] <= ((_colorComp[0]+margin < 255)? _colorComp[0]+margin : 255) && 
                         targetColor[0] >= ((_colorComp[0]-margin > 0)? _colorComp[0]-margin : 0))&&//red
                        (targetColor[1] <= ((_colorComp[1]+margin < 255)? _colorComp[1]+margin : 255)
                        && targetColor[1] >= ((_colorComp[1]-margin > 0)? _colorComp[1]-margin : 0))&&//green    
                        (targetColor[2] <= ((_colorComp[2]+margin < 255)? _colorComp[2]+margin : 255) 
                        && targetColor[2] >= ((_colorComp[2]-margin > 0)? _colorComp[2]-margin : 0))){//blue
                    filteredImage.setRGB(x,y,_highlightColor.getRGB());
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
                }
            }
        }
        generateCenterOfMass();*/
    }
}
