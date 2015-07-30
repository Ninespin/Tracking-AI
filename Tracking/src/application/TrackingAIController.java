/*
 * The MIT License
 *
 * Copyright 2015 Eloi.
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
package application;

import IO.FrameStream;
import Shapes.PatternDetector;
import Shapes.Template;
import UI.Display;
import UI.fx.FXErrorMessage;
import UI.fx.FXOptionPane;
import graphics.Filter;
import graphics.FrameProcessor;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import remote.controller.RemoteController;
import tracking.Tracking;

/**
 * Class used to initialyse and controll the main loop and other options.
 *
 * @author Arnaud Paré-Vogt & Jérémi Cyr
 */
public class TrackingAIController {

    private ApplicationState currentState;

    //config stuf
    private String templatePath;
    private String imagePath;
    //

    private Template template;

    private RemoteController remote;

    //parts of the Loop
    private FrameStream frameStream;
    private Tracking tracking;
    PatternDetector patternDetector;
    FrameProcessor frameProcessor;
    //
    
    //TODO remove or change Display
    @Deprecated
    private Display display;

    //TODO wrap all config elements in a pre-prepared bundle
    public TrackingAIController(String templatePath, String imagePath, RemoteController remote, Display display) {
        currentState = ApplicationState.EMPTY;
        this.templatePath = templatePath;
        this.imagePath = imagePath;
        this.remote = remote;

    }

    /**
     * Initialyses the application, creating the loop objects.
     *
     * @return the succes or fail of the initialization procces
     */
    public boolean init() {
        //load the template
        BufferedImage templateImage;
        try {
            templateImage = ImageIO.read(new File(templatePath));
        } catch (IOException ex) {
            FXErrorMessage.sendMessage("The template could not be loaded.","Error",ex);
            return false;
        }
        template = new Template(templateImage);
        //

        //start FrameStream
        if (remote.isConnected()) {
            frameStream = new FrameStream(remote);//creates a server-connected version
        } else {
            int i = FXOptionPane.showOptionDialog("Warning","Server not Connected!", "The Server is not connected. Do you wish to loat the files from a local repository?", "Yes");
            if (i == 0) {
                try {
                    frameStream = new FrameStream(imagePath);// <-- le path
                } catch (IOException ex) {
                    FXErrorMessage.sendMessage("The images could not be loaded.", "Error", ex);
                    return false;
                }
            } else {
                return false;
            }
        }
        //

        Filter f = new Filter(Color.red,0);
        
        tracking = new Tracking(template, frameStream);
        patternDetector = new PatternDetector(null, tracking);
        frameProcessor = new FrameProcessor(f, display, patternDetector);
        frameStream.setOutput(frameProcessor);
        frameStream.start();
        tracking.start();
        patternDetector.start();
        display.start(tracking);

        frameStream.sendImage();

        currentState = ApplicationState.INITIALYSED;
        return true;
    }

    /**
     * Starts the application by requesting the first image
     */
    public void start() {
        currentState = ApplicationState.RUNNING;
    }

    /**
     * Stops the application.
     *
     * @deprecated not supported yet.
     */
    public void stop() {
        currentState = ApplicationState.STOPPED;
    }

    //- SETTERS AND GETTERS
    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }
    
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    
}
