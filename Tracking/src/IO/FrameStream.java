/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IO;

import graphics.Frame;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import remote.controller.RemoteController;

/**
 * Class that reads images and videos and sends them to be processed.
 * @author Jeremi Cyr & Aranud Paré-Vogt
 */
public class FrameStream implements Runnable{
    
    private static final String IMAGE_EXCTENTION = ".jpg";
    
    private List<IImageProcessor> processors;
    
    private IImageGetter imageGetter;
    
    private Thread thread;
    private boolean running;
    
    private boolean readyToSendImages = false;
    
    public FrameStream(String pathName) throws IOException{
        File f = new File(pathName);
        if(!f.isDirectory()){
            throw new IOException("The provided file is not a directory.");
        }
        
        List<BufferedImage> images = getImages(getImagesInDirectory(f));
        Iterator<BufferedImage> i = images.iterator();
        processors = new ArrayList<>();
        thread = new Thread(this,"Image Stream input thread");
        imageGetter = ()->{
            if(i.hasNext()){
                return i.next();
            }else{
                return null;
            }
        };
    }
    
    public FrameStream(RemoteController remote){
        processors = new ArrayList<>();
        thread = new Thread(this,"Image Stream input thread");
        imageGetter = ()->{
            return remote.getImage();
        };
    }
    
    private List<BufferedImage> getImages(List<File> imagesFiles){
        ArrayList<BufferedImage> images = new ArrayList<>();
        for (File imagesFile : imagesFiles) {
            try {
                images.add(ImageIO.read(imagesFile));
            } catch (IOException ex) {
                System.out.println("Image "+imagesFile.getAbsolutePath()+" could not be read!");
            }
        }
        return images;
    }
    
    private List<File> getImagesInDirectory(File directory){
        ArrayList<File> images = new ArrayList<>();
        for (File listFile : directory.listFiles()) {
            if(listFile.getName().endsWith(IMAGE_EXCTENTION)){
                images.add(listFile);
            }
        }
        return images;
    }
    
    public void setOutput(IImageProcessor processor){
        processors.add(processor);
    }
    
    public void start(){
        running = true;
        thread.start();
    }
    
    /**
     * Tells the FrameStream to send the next image.
     */
    public void sendImage(){
        synchronized(this){
            readyToSendImages = true;
            this.notifyAll();
        }
    }

    @Override
    public void run() {
        while(running){
            try {
                synchronized(this){
                    while(!readyToSendImages){
                        this.wait();
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(FrameStream.class.getName()).log(Level.SEVERE, null, ex);
            }
            BufferedImage img = imageGetter.getImage();
            for (IImageProcessor processor : processors) {
                processor.process(new Frame(img));
            }
            readyToSendImages=false;
        }
    }
    
    
    private interface IImageGetter{
        public abstract BufferedImage getImage();
    }
}
