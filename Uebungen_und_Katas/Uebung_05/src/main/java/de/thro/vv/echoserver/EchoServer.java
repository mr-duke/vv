package de.thro.vv.echoserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class EchoServer {

    private static int counterConnections = 0;

    public static void main(String[] args){
        try(ServerSocket serverSocket = new ServerSocket(10025)){
            while(true){
                try{
                    Socket socket = serverSocket.accept();
                    Thread thread = new Thread(() -> handle(socket));
                    thread.start();
                } catch (IOException e){
                    System.out.println("ERROR: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    private static void handle (Socket socket){
        counterConnections++;
        System.out.println("Thread No. " + counterConnections + " has started");
        try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter( new OutputStreamWriter(socket.getOutputStream()))) {

            Scanner sc = new Scanner(in);

            String message = null;
            while((message = sc.nextLine()) != null ){
                System.out.println(message);
                out.println("\n" + message);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
}
