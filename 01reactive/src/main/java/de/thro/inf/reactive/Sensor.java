package de.thro.inf.reactive;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Sensor {

    private SensorArt sensorArt;

    public enum SensorArt {LINKS, RECHTS};
    private Sensor(SensorArt sensorArt) {
        this.sensorArt = sensorArt;
    }

    private static final Logger SYSTEM_LOGGER = Logger.getLogger("systemLogger");
    private static final Logger EVENTS_LOGGER = Logger.getLogger("eventLogger");
    private static BlockingQueue<String> outgoing = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        Sensor sensor = new Sensor(SensorArt.RECHTS);
        Thread clientThread = new Thread(() -> createClientSocket(sensor));
        clientThread.start();

        Scanner scanner = new Scanner(System.in);
        while (true){
            readFromInput(scanner);
        }
    }

    private static void createClientSocket(Sensor sensor) {
        Socket socket = null;
        try {
            socket = new Socket(InetAddress.getLocalHost(), 10025);
            socket.setSoTimeout(3*1000);

        } catch (IOException ex) {
            SYSTEM_LOGGER.error(ex.getMessage());
            closeSocket(socket, sensor);
            return;
        }

        try (OutputStream output = socket.getOutputStream()) {

            while (true) {
                String jsonString = outgoing.take();
                output.write(jsonString.getBytes());
                output.write("\r\n".getBytes());
                output.flush();
            }

        } catch (IOException | InterruptedException ex) {
            SYSTEM_LOGGER.error(ex.getMessage());
            //closeSocket(socket, sensor);
        }
    }

    private static void readFromInput (Scanner scanner){
        String message = scanner.nextLine();

        // Neue Zeile einlesen, falls JSON Syntax Fehler
        Gson gson = new Gson();
        try {
            gson.fromJson(message, Ereignis.class);
        } catch (JsonSyntaxException ex) {
            SYSTEM_LOGGER.error("JSON Syntax-Fehler --> Lese nächste Zeile");
            return;
        }

        try {
            outgoing.put(message);
        } catch (InterruptedException ex) {
            SYSTEM_LOGGER.error(ex.getMessage());
        }
    }

    private static void closeSocket(Socket socket, Sensor sensor){
        try {
            socket.close();
            SYSTEM_LOGGER.info(String.format("Client-Sensor %s geschlossen", sensor.sensorArt));
        } catch (IOException ex) {
            SYSTEM_LOGGER.error(ex.getMessage());
        }
    }
}
