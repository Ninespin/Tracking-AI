/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IO;

import Shapes.Template;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Class used to write shapes is the disc, for saving purposes
 * @author Arnaud & Jérémi
 */
public class ShapeWriter {
    
    private File directory;
    private Thread outThread;
    private boolean running;
    
    private final List<PrintableObject> buffer;
    
    public ShapeWriter(String directory){
        this.directory = new File(directory);
        buffer = new LinkedList<>();
        running = true;
        outThread = new Thread(()->{
            while(running){
                while(buffer.isEmpty()){
                    try {
                        synchronized (buffer) {
                            buffer.wait();
                        }
                    } catch (InterruptedException ex) {}
                }
                writeAll();
            }
        },"Shape Writer Thread");
    }
    
    public void start(){
        outThread.start();
    }
    
    public void save(Template tmp, String name){
        buffer.add(new PrintableObject(name, tmp));
        synchronized (buffer) {
            buffer.notifyAll();
        }
    }
    
    private void writeAll(){
        for (Iterator<PrintableObject> iterator = buffer.iterator(); iterator.hasNext();) {
            PrintableObject object = iterator.next();
            
            File outFile = new File(directory, object.getName()+".bin");
            
            try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(outFile))){
                out.writeObject(object.getTemplate());
            } catch (IOException ex) {
                Logger.getLogger(ShapeWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try{
                ImageIO.write(object.getTemplate().toImage(), "png", new File(directory,object.getName()+".png"));
            } catch (IOException ex) {
                Logger.getLogger(ShapeWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
            iterator.remove();
        }
    }
    
    private class PrintableObject{
        private String name;
        private Template o;

        public PrintableObject(String name, Template t) {
            this.name = name;
            this.o = t;
        }

        public String getName() {
            return name;
        }

        public Template getTemplate() {
            return o;
        }
    }
}
