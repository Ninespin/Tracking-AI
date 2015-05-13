/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * Class that reads images and videos and sends them to be processed.
 * @author jeremi
 */
public class FrameStream implements Runnable{
    
    private static final String IMAGE_EXCTENTION = ".png";
    
    private List<BufferedImage> images;
    private List<IImageProcessor> processors;
    
    private Thread thread;
    
    public FrameStream(String pathName) throws IOException{
        File f = new File(pathName);
        if(!f.isDirectory()){
            throw new IOException("The provided file is not a directory.");
        }
        
        images = getImages(getImagesInDirectory(f));
        processors = new ArrayList<>();
        thread = new Thread(this,"Image Stream input thread");
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
        thread.start();
    }

    @Override
    public void run() {
        for (BufferedImage next : images) {
            for (IImageProcessor processor : processors) {
                processor.process(next);
            }
        }
    }
    
    
    
}
