/*
 * The MIT License
 *
 * Copyright 2015 Arnaud Paré-Vogt & Jérémi Cyr.
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
package neuralnet;

import java.util.List;
import math.Function;

/**
 *
 * @author Arnaud
 */
public class Neuron {
    
    private List<Synapse> out;
    private Function activationFunction;
    
    private double currentIn;

    public Neuron(List<Synapse> out,Function activationFunction) {
        this.out = out;
        this.activationFunction = activationFunction;
    }
    
    public void clear(){
        currentIn = 0;
    }
    
    public void addToValue(double value){
        currentIn += value;
    }
    
    public void send(){
        for (Synapse out1 : out) {
            out1.carry(activationFunction.f(currentIn));
        }
    }
}
