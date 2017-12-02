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
    private ServerSocket server;
    private Thread thread;
    private ChatServerThread[] clients = new ChatServerThread[50];
    int clientCount = 0;
    
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
                stop();
            }
        }
    }
    private int findClient(int ID) {
        for(int i=0;i<clientCount;i++) {
            if(clients[i].getID()==ID)
                return i;
        }
        return -1;
    }
    public synchronized void handle(int ID, String input) {
        if(input.equals("/quit")) {
            clients[findClient(ID)].send("/quit");
            remove(ID);
        }
        else {
            for(int i=0;i<clientCount;i++) {
                clients[i].send(ID+": "+input);
            }
        }
    }
    public synchronized void remove(int ID) {
        int pos = findClient(ID);
        if(pos>=0) {
            ChatServerThread toTerminate = clients[pos];
            System.out.println("Removing client thread " + ID + " at " + pos);
            if(pos<clientCount-1) {
                for(int i=pos+1;i<clientCount;i++) {
                    clients[i-1] = clients[i];
                }
            }
            clientCount--;
            try{
                toTerminate.close();
            }
            catch(Exception e) {
                System.out.println(e);
            }
            toTerminate.stop();
        }
    }
    public void addThread(Socket socket) {
        if(clientCount<clients.length) {
            System.out.println("Client Accepted: "+socket);
            clients[clientCount] = new ChatServerThread(this, socket);
            try{
                clients[clientCount].open();
                clients[clientCount].start();
                clientCount++;
            }
            catch(Exception e) {
                System.out.println(e);
            }
        }
        else {
            System.out.println("Client refused: maximum " +clients.length+ " reached");
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
