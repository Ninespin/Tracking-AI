/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import java.awt.Color;

/**
 *
 * @author jeremi
 */
public class Filter {//color+errormargin
    private final Color filterColor;//color to look for
    private final int precision;//accepted margin in %
    
    public Filter(Color _filterColor, int _margin){
        filterColor = _filterColor;
        precision = _margin;
    }
    
    public Color getColor(){
        return filterColor;
    }
    public int getPrecision(){
        return precision;
    }
}
