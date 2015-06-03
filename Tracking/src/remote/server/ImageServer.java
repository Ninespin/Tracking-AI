/*
 * The MIT License
 *
 * Copyright 2015 Arnaud Paré-Vogt.
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
package remote.server;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.imageio.ImageIO;
import remote.utility.IImageListener;
import remote.utility.IStringListener;

/**
 *
 * @author Arnaud Paré-Vogt
 */
public class ImageServer{

    public static final int MAX_PORT_INDEX = 0xffff;

    public enum ServerStatus {

        FINE("Connected"), NO_CONNECTION("Not connected");

        String text;

        private ServerStatus(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    int port;
    Socket socket;

    DataInputStream in;
    DataOutputStream pw;

    ServerStatus currentStatus;

    Thread socketListenerThread, messageListenerThread,refreshThread;

    IImageListener output;

    boolean running;

    public ImageServer(int port, IImageListener output) {
        this.port = port;
        this.output = output;
        currentStatus = ServerStatus.NO_CONNECTION;
        running = true;
    }

    public void shutDown() {
        try {
            pw.close();
        } catch (IOException ex) {}
        try {
            in.close();
        } catch (IOException ex) {
        }
        running = false;
    }

    public void listenForConnection() {
        socketListenerThread = new Thread(() -> {
            while (socket == null) {
                try {
                    generateSocket();
                } catch (IOException ex) {
                }
            }
            listen();
        }, "SocketListener");
        socketListenerThread.start();
    }
    
    private void refresh(){
        
    }

    private void generateSocket() throws IOException {
        ServerSocket sSocket = new ServerSocket(port);
        socket = sSocket.accept();
        in = new DataInputStream(socket.getInputStream());
        pw = new DataOutputStream(socket.getOutputStream());
        currentStatus = ServerStatus.FINE;
    }

    public void write(String s) {
        System.out.println("No.");
    }

    public void listen() {
        messageListenerThread = new Thread(() -> {
            while (running) {
                try {
                    File file = new File("temp.jpg");
                    file.createNewFile();
                    try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file))) {
                        int count;
                        byte[] buffer = new byte[8192];
                        while ((count = in.read(buffer)) > 0){
                            dos.write(buffer, 0, count);
                            dos.flush();
                        }
                        dos.close();
                    }
                    BufferedImage i = ImageIO.read(file);
                    output.imageRecieved(i);
                } catch (IOException ex) {
                    System.out.println("null at Iserver");
                    output.imageRecieved(null);
                }
            }
        }, "Message Listener");
        messageListenerThread.start();
    }

    public ServerStatus getCurrentStatus() {
        return currentStatus;
    }

    public String getStatus() {
        return currentStatus + " - port : " + port;
    }
}
