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

import java.util.ArrayList;
import java.util.List;
import math.Function;

/**
 *
 * @author Arnaud Paré-Vogt
 */
public class NeuralNet {
    
    List<List<Neuron>> layers;
    
    
    public NeuralNet(int ... numbersOfNeurons){
        layers = new ArrayList<>();
        for (int i = 0; i < numbersOfNeurons.length; i++) {
            layers.add(new ArrayList<>());
            for (int j = 0; j < numbersOfNeurons[i]; j++) {
                //Use reverse mode [...]
                //layers.get(i).add(new Neuron(null, Function.sigmoid))
            }
        }
        
    }
    
}
