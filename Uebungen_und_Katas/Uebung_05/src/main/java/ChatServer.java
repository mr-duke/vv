package de.thro.inf.katas.sockets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Musterloesung für die erste echte Uebungsaufgabe.
 * Diese dient auch dazu, die naechste Uebung machen zu koennen.
 */
public class ChatServer {
    // Achtung noch sehr schlechtes Design. Da alles statisch!!!
    private static List<Socket> allClients = new LinkedList<>();

    public static void main(String[] args) {
        Logger log = Logger.getLogger("CHAT");

        Executor exe = Executors.newCachedThreadPool();
        log.info("Starte Server mit Port 10025");

        try (ServerSocket server = new ServerSocket(10025)) {

            log.info("Server erfolgreich gestartet");
            while (true) {
                log.info("Warte auf Client-Anfrage");
                Socket client = server.accept();
                allClients.add(client);
                log.info("Neuer Client akzeptiert");
                exe.execute(() -> processClient(client));
            }
        }catch (IOException io) {
            log.log(Level.SEVERE, io.getMessage(), io);
        }
        log.info("Server terminiert");
    }

    public static void processClient(Socket client) {
        Logger log = Logger.getLogger("CHAT");

        try (InputStream in = client.getInputStream();
             OutputStream out = client.getOutputStream()) {

            Scanner scan = new Scanner(in);

            while (true) {
                String zeile = scan.nextLine();
                log.info("From Client:" +zeile);
                if (zeile.equals("QUIT")) {
                    log.info("Removing Client:" + client);
                    removeClient(client);
                    return;
                }
                toAllClients("Answer:" + zeile, client);
            }
        }
        catch(IOException x) {
            log.log(Level.SEVERE, x.getMessage(),x);
        }
    }

    public static void toAllClients(String message, Socket from) {
        Logger log = Logger.getLogger("CHAT");
         try {
             for(Socket client: allClients) {
                 if (client == from) continue;

                 OutputStream out = client.getOutputStream();
                 out.write(message.getBytes());
                 out.write("\r\n".getBytes());
                 out.flush();
             }
         }
         catch(IOException io) {
             log.log(Level.SEVERE, io.getMessage(),io);
         }
    }

    public static void removeClient(Socket client) {
        allClients.remove(client);
    }
}
