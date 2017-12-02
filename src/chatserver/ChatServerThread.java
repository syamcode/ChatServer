/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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
    int ID = -1;
    DataInputStream inpStream;
    DataOutputStream outStream;
    public ChatServerThread(ChatServer _server, Socket _socket) {
        super();
        server = _server;
        socket = _socket;
        ID = socket.getPort();
    }
    public void send(String msg) {
        try{
            outStream.writeUTF(msg);
            outStream.flush();
        }
        catch(Exception e){
            System.out.println(e);
            server.remove(ID);
            stop();
        }
    }
    public int getID() {
        return ID;
    }
    public void run() {
        System.out.println("Server Thread "+ID+" running");
        while(true) {
            try {
                server.handle(ID, inpStream.readUTF());
            } catch (Exception e) {
                System.out.println(e);
                server.remove(ID);
                stop();
            }
        }
    }
    
    public void open() throws IOException {
        inpStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        outStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }
    
    public void close() throws IOException {
        if(socket!=null) {
            socket.close();
        }
        if(inpStream!=null) {
            inpStream.close();
        }
        if(outStream!=null) {
            outStream.close();
        }
    }
}
