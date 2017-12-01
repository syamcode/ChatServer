/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author syamcode
 */
public class ChatServer implements Runnable{
    ServerSocket server;
    Thread thread;
    ChatServerThread client;
    
    public void startServer(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("Server started "+server);
            start();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    @Override
    public void run() {
        while(thread!=null) {
            try{
                System.out.println("Waiting for client ...");
                addThread(server.accept());
            }
            catch(Exception e) {
                System.out.println(e);
            }
        }
    }
    public void addThread(Socket socket) {
        System.out.println("Client Accepted: "+socket);
        client = new ChatServerThread(this, socket);
        try{
            client.open();
            client.start();
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }
        public void start() {
        if(thread==null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() {
        if(thread!=null) {
            thread.stop();
            thread = null;
        }
    }
    public static void main(String[] args) {
        ChatServer chat = new ChatServer();
        chat.startServer(5001);
    }
}
