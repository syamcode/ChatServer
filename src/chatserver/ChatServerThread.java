/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author syamcode
 */
public class ChatServerThread extends Thread{
    Socket socket;
    ChatServer server;
    int ID;
    DataInputStream inpStream;
    
    public ChatServerThread(ChatServer _server, Socket _socket) {
        server = _server;
        socket = _socket;
        ID = socket.getPort();
    }
    
    public void run() {
        System.out.println("Server Thread "+ID+" running");
        while(true) {
            try {
                System.out.println(inpStream.readUTF());
            } catch (Exception e) {
            }
        }
    }
    
    public void open() throws IOException {
        inpStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
    }
    
    public void close() throws IOException {
        if(socket!=null) {
            socket.close();
        }
        if(inpStream!=null) {
            inpStream.close();
        }
    }
}
