/*
 * The MIT License
 *
 * Copyright 2015 Jérémi Cyr & Arnaud Paré-Vogt.
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
package IO;

import graphics.Frame;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import remote.controller.RemoteController;

/**
 * Class that reads images and videos and sends them to be processed.
 *
 * @author Jeremi Cyr & Aranud Paré-Vogt
 */
public class FrameStream implements Runnable {

    private static final String IMAGE_EXCTENTION = ".jpg";

    private List<IImageProcessor> processors;

    private IImageGetter imageGetter;

    private Thread thread;
    private boolean running;

    private boolean readyToSendImages = false;

    /**
     * Creates an instance of <code>FrameStream</code> that reads the images
     * from the specified directory. All images ending with the extention
     * <code>IMAGE_EXCTENTION</code> will be loaded into memory upon creation.
     * (This means that it is a BAD idea to load 2000 images)
     *
     * @param pathName The name of the path. It must be a directory, and it
     * should contain images of the extention <code>IMAGE_EXCTENTION</code>.
     * @throws IOException if the specified path is not a directory.
     */
    public FrameStream(String pathName) throws IOException {
        File f = new File(pathName);
        if (!f.isDirectory()) {
            throw new IOException("The provided file is not a directory.");
        }

        List<BufferedImage> images = getImages(getImagesInDirectory(f));
        Iterator<BufferedImage> i = images.iterator();
        processors = new ArrayList<>();
        thread = new Thread(this, "Image Stream input thread");
        imageGetter = () -> {
            if (i.hasNext()) {
                return i.next();
            } else {
                return null;
            }
        };
    }

    /**
     * Creates a <code>FrameStream</code> object attatched to a
     * <code>Remote</code> object. The <code>FrameStream</code> will get its
     * images from the <code>Remote</code> object.
     *
     * @param remote The <code>Remote</code> object to connect to.
     */
    public FrameStream(RemoteController remote) {
        processors = new ArrayList<>();
        thread = new Thread(this, "Image Stream input thread");
        imageGetter = () -> {
            return remote.getImage();
        };
    }

    /**
     * Method used by the constructor to load the given images.
     *
     * @param imagesFiles a list of images under <code>File</code> form to load.
     * @return A list of <code>BufferedImages</code> loaded form the files.
     */
    private List<BufferedImage> getImages(List<File> imagesFiles) {
        ArrayList<BufferedImage> images = new ArrayList<>();
        for (File imagesFile : imagesFiles) {
            try {
                images.add(ImageIO.read(imagesFile));
            } catch (IOException ex) {
                System.out.println("Image " + imagesFile.getAbsolutePath() + " could not be read!");
            }
        }
        return images;
    }

    /**
     * Gets all of the Images of teh extention <code>IMAGE_EXCTENTION</code> in
     * the specified directory, and returns them as <code>File</code>s objects.
     *
     * @param directory The path to the directory of images.
     * @return A list of files that are images ending with
     * <code>IMAGE_EXCTENTION</code>
     */
    private List<File> getImagesInDirectory(File directory) {
        ArrayList<File> images = new ArrayList<>();
        for (File listFile : directory.listFiles()) {
            if (listFile.getName().endsWith(IMAGE_EXCTENTION)) {
                images.add(listFile);
            }
        }
        return images;
    }

    /**
     * Adds an imageProcessor as an output of this frameStream
     *
     * @param processor The <code>IImageProcessor</code> to add as output
     */
    public void setOutput(IImageProcessor processor) {
        processors.add(processor);
    }

    /**
     * Starts the <code>FrameStream</code>'s threads, allowing it to do stuff
     */
    public void start() {
        running = true;
        thread.start();
    }

    /**
     * Tells the FrameStream to send the next image. Note that it has to be
     * started for this to work.
     */
    public void sendImage() {
        synchronized (this) {
            readyToSendImages = true;
            this.notifyAll();
        }
    }

    /**
     * The run method of <code>FrameStream</code>. It waits for someone to call
     * the <code>sendImage()</code> method, and then sends an image to all added
     * <code>IImageListeners</code>
     */
    @Override
    public void run() {
        while (running) {
            while (!readyToSendImages) {
                try {
                    synchronized (this) {
                        this.wait();
                    }
                } catch (InterruptedException ex) {
                    //if we fail to wait, very well, we will loop a much as we can!
                }
            }
            BufferedImage img = imageGetter.getImage();
            for (IImageProcessor processor : processors) {
                processor.process(new Frame(img));
            }
            readyToSendImages = false;
        }
    }

    /**
     * Private interface used to simplify the construction of the class. Is
     * defines a method to get an image, and it is used in the constructor.
     */
    private interface IImageGetter {

        public abstract BufferedImage getImage();
    }
}
