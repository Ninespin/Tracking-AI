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
 * @author jeremi
 */
public class FrameStream implements Runnable{
    
    private static final String IMAGE_EXCTENTION = ".jpg";
    
    private List<IImageProcessor> processors;
    
    private IImageGetter imageGetter;
    
    private Thread thread;
    private boolean running;
    
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
                System.out.println("Imege "+imagesFile.getAbsolutePath()+" could not be read!");
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

    @Override
    public void run() {
        while(running){
            BufferedImage img = imageGetter.getImage();
            for (IImageProcessor processor : processors) {
                processor.process(new Frame(img));
            }
            try {
                thread.sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(FrameStream.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    private interface IImageGetter{
        public abstract BufferedImage getImage();
    }
}
