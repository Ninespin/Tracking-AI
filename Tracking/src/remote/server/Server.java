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
import remote.utility.IStringListener;

/**
 *
 * @author Arnaud Paré-Vogt
 */
public class Server {

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

    BufferedReader br;
    PrintWriter pw;

    ServerStatus currentStatus;

    Thread socketListenerThread, messageListenerThread,refreshThread;

    IStringListener output;

    boolean running;

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
            } catch (IOException ex) {
            }
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
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        pw = new PrintWriter(socket.getOutputStream(), true);
        currentStatus = ServerStatus.FINE;
    }

    public void write(String s) {
        pw.println(s);
    }

    public void listen() {
        messageListenerThread = new Thread(() -> {
            while (running) {
                try {
                    if(br.ready()){
                        String s = br.readLine();
                        output.stringRecieved(s);
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
