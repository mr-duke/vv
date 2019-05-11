package de.thro.inf.reactive;

import com.google.gson.Gson;
import configuration.FromConsole;
import configuration.IConfiguration;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {

    private static final Logger SYSTEM_LOGGER = Logger.getLogger("systemLogger");
    private static final Logger EVENTS_LOGGER = Logger.getLogger("eventLogger");

    private static BlockingQueue<Ereignis> incoming = new LinkedBlockingQueue<>();
    private static Executor exec = Executors.newCachedThreadPool();
    private static IConfiguration config = new FromConsole();

    public static void main(String[] args) {
        Thread serverSocketThread = new Thread(() -> provideServerSocket());
        serverSocketThread.start();

        Mitarbeiterverwaltung mitarbeiterverwaltung = Mitarbeiterverwaltung.getMitarbeiterverwaltung();

        while (true){
            try {
                Ereignis ereignis = incoming.take();
                mitarbeiterverwaltung.notify(ereignis);

            }
            catch (InterruptedException ex) {
                SYSTEM_LOGGER.error(ex.getMessage());
            }
        }
    }

    public static void provideServerSocket() {
        try(ServerSocket server = new ServerSocket(config.getPortNumber())){
            SYSTEM_LOGGER.info("Server erfolgreich gestartet");

            while (true) {
                SYSTEM_LOGGER.info("Warten auf Client-Anfrage ... ");
                Socket client = server.accept();
                SYSTEM_LOGGER.info("Neuer Client akzeptiert");
                exec.execute(() -> readFromSocket(client));
            }

        } catch (IOException ex) {
            SYSTEM_LOGGER.error(ex.getMessage());
        }
        SYSTEM_LOGGER.info("Server beendet");
    }

    public static void readFromSocket(Socket client) {
        try(InputStream inputStream = client.getInputStream()) {
            Gson gson = new Gson();
            Scanner scanner = new Scanner(inputStream);

            while (true) {
                String message = scanner.nextLine();

                if (message.equals("QUIT")){
                    client.close();
                    return;
                }
                Ereignis ereignis = gson.fromJson(message, Ereignis.class);
                incoming.put(ereignis);
            }

        } catch (IOException | InterruptedException ex) {
            SYSTEM_LOGGER.error(ex.getMessage());
        }
    }
}
