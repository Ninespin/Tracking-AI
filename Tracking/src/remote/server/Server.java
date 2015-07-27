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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import remote.utility.IStringListener;

/**
 *
 * @author Arnaud Paré-Vogt
 */
public class Server {

    public static final int MAX_PORT_INDEX = 0xffff;

    public enum ServerStatus {

        FINE("Connected"), NO_CONNECTION("Not connected"),TIMOUT_WAITING("Waiting for an answer...");

        String text;

        private ServerStatus(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    private int port;
    private Socket socket;

    private long packetEleapsedTime = 0;//not usefull currently
    /** The time the server takes to tieout in seconds*/
    public static final long TIMOUT_TIME = 10;
    private boolean rescievedReturnPacket = false;
    boolean timeExceded;
    /** for timing if the packets are returned in time */
    private ScheduledExecutorService timoutTimer = Executors.newScheduledThreadPool(1);
    
    private BufferedReader br;
    private PrintWriter pw;

    private ServerStatus currentStatus;

    private Thread socketListenerThread, messageListenerThread,refreshThread;

    private IStringListener output;

    private boolean running;
    
    

    public Server(int port, IStringListener output) {
        this.port = port;
        this.output = output;
        currentStatus = ServerStatus.NO_CONNECTION;
        running = true;
    }

    public void shutDown() {
        if(pw != null){
            pw.write("Q:");
            pw.close();
        }
        if(br!=null){
            try {
                br.close();
            } catch (IOException ex) {}
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
            startRefreshCycle();
        }, "SocketListener");
        socketListenerThread.start();
    }
    
    private void startRefreshCycle(){
        timeExceded = false;
        
        final Runnable timer = ()->{
            synchronized(this){
                timeExceded = true;
                this.notifyAll();
            }
        };
        
        Runnable r = ()->{
            while(running){
                this.write("HI:");
                timoutTimer.schedule(timer, TIMOUT_TIME, TimeUnit.SECONDS);
                while(!rescievedReturnPacket && !timeExceded){
                    synchronized(this){
                        try {
                            this.wait();
                        } catch (InterruptedException ex) {}
                    }
                }
                if(rescievedReturnPacket){
                    rescievedReturnPacket = false;
                    this.currentStatus = ServerStatus.FINE;//we are good and responsive
                    while(!timeExceded){//we wait the rest of the time, we do not want to overload the socket
                        synchronized(this){
                            try {
                                this.wait();
                            } catch (InterruptedException ex) {//if this happens it is probably because we shall stop.
                                timeExceded = true;
                                break;
                            }
                        }
                    }
                    timeExceded = false;
                }else if(timeExceded){//this is bad!
                    this.currentStatus = ServerStatus.TIMOUT_WAITING;
                    timeExceded = false;
                    while(!rescievedReturnPacket){//we wait for the packet to come
                        synchronized(this){
                            try {
                                this.wait();
                            } catch (InterruptedException ex) {//if we are interrupted it is probably the the program wants us to stop.
                                rescievedReturnPacket = true;
                                break;
                            }
                        }
                    }
                    rescievedReturnPacket = true;
                    this.currentStatus = ServerStatus.FINE;//we are responsive again! :)
                }
            }
        };
        
        refreshThread = new Thread(r, "Timout checker thread.");
        refreshThread.start();
    }

    private void generateSocket() throws IOException {
        ServerSocket sSocket = new ServerSocket(port);
        socket = sSocket.accept();
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        pw = new PrintWriter(socket.getOutputStream(), true);
        currentStatus = ServerStatus.FINE;
    }

    public void write(String s) {
        synchronized(pw){
            pw.println(s);
        }
    }

    public void listen() {
        messageListenerThread = new Thread(() -> {
            while (running) {
                try {
                    if(br.ready()){
                        String s = br.readLine();
                        System.out.println(s);
                        if(s.startsWith("Q:")){
                            shutDown();
                        }else if (s.startsWith("HI:")){
                            rescievedReturnPacket = true;
                            synchronized(this){
                                this.notifyAll();
                            }
                        }else{
                            output.stringRecieved(s);
                        }
                    }else{
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {}
                    }
                } catch (IOException ex) {
                    output.stringRecieved(null);
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
