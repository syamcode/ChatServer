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
public class ChatServer {
    ServerSocket server;
    Socket socket;
    DataInputStream inpStream;
    public static void main(String[] args) {
        ChatServer chat = new ChatServer();
        chat.startServer(5001);
    }
    
    public void startServer(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("Server started "+server);
            System.out.println("Waiting for client ...");
            socket = server.accept();
            System.out.println("Client Accepted: "+socket);
                
            boolean connect = true;
            inpStream = new DataInputStream(socket.getInputStream());
            while(connect) {
                try {
                    String str = inpStream.readUTF();
                    System.out.println("Message: "+str);
                    connect = !(str.equals("/quit"));
                }
                catch(IOException e) {
                    connect = false;
                    System.out.println(e);
                }
            }
            close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    public void close() throws IOException {
        if(inpStream!=null) {
            inpStream.close();
        }
        if(socket!=null) {
            socket.close();
        }
    }
}