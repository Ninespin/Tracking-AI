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
package IO.debug;

import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

/**
 * 
 * @author Aranud Paré-Vogt
 */
public class StandardOut extends PrintStream{

    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    
    public StandardOut(OutputStream out) {
        super(out,true);
    }

    private String getPrefix(){
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int iMax = elements.length-1;
        for (int i = 3; ; i++) {//gives us only the elements that have called the println
            sb.append(elements[i]);
            if(i==iMax){
                sb.append("]");
                break;
            }
            sb.append(",");
        }
        String s = sb.toString();
        return "at ["+dateFormat.format(Calendar.getInstance().getTime())+"], on "+s+"\n -> ";
    }
    
    @Override
    public void println(Object o) {
        synchronized(this){
            super.println(getPrefix() + o ); //To change body of generated methods, choose Tools | Templates.
        }
    }
    
    @Override
    public void println(String s) {
        synchronized(this){
            super.println(getPrefix() + s ); //To change body of generated methods, choose Tools | Templates.
        }
    }
    
    @Override
    public void println(int i) {
        synchronized(this){
            super.println(getPrefix() + i ); //To change body of generated methods, choose Tools | Templates.
        }
    }
    
    @Override
    public void println(double d) {
        synchronized(this){
            super.println(getPrefix() + d ); //To change body of generated methods, choose Tools | Templates.
        }
    }
    
    @Override
    public void println(float f) {
        synchronized(this){
            super.println(getPrefix() + f ); //To change body of generated methods, choose Tools | Templates.
        }
    }
    
    @Override
    public void println(long l) {
        synchronized(this){
            super.println(getPrefix() + l ); //To change body of generated methods, choose Tools | Templates.
        }
    }

    @Override
    public void println(char[] x) {
        synchronized(this){
            super.println(getPrefix() + x ); //To change body of generated methods, choose Tools | Templates.
        }
    }
    
    @Override
    public void println(char c) {
        synchronized(this){
            super.println(getPrefix() + c ); //To change body of generated methods, choose Tools | Templates.
        }
    }
    
    
}
