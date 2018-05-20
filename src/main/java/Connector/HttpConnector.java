package Connector;

import Connector.Process.HttpProcessor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpConnector implements Runnable {

    volatile boolean stop = false;

    public void run() {
        ServerSocket serverSocket = null;
        int port = 8080;
        try {
            serverSocket = new ServerSocket(port,1, InetAddress.getByName("127.0.0.1"));
        }catch (IOException e){
            e.printStackTrace();
        }
        while (!stop){
            //waiting the request
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (Exception e){
                continue;
            }
            HttpProcessor httpRequestProcess = new HttpProcessor(this);
            httpRequestProcess.process(socket);
        }
    }
    public void start(){
        Thread thread = new Thread(this);
        thread.start();
    }
}
