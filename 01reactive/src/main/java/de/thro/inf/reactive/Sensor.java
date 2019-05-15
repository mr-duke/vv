package de.thro.inf.reactive;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import configuration.FromConsole;
import configuration.FromFile;
import configuration.IConfiguration;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Sensor {

    private static final Logger SYSTEM_LOGGER = Logger.getLogger("systemLogger");
    private static final String SHUTDOWN_KEY = "QUIT";
    private static BlockingQueue<String> outgoing = new LinkedBlockingQueue<>();
    /* Hier wird Config-Art festgelegt: FromConsole oder FromFile*/
    private static IConfiguration config = new FromConsole();

    private SensorArt sensorArt;
    public enum SensorArt {LINKS, RECHTS};
    private Sensor(SensorArt sensorArt) {

        this.sensorArt = sensorArt;
    }

    public static void main(String[] args) {
        Sensor sensor = new Sensor(config.getSensorArt());
        Thread clientThread = new Thread(() -> createClientSocket(sensor));
        clientThread.start();

        Scanner scanner = new Scanner(config.getInputStream());
        while (true){
            readFromInput(scanner);
        }
    }

    private static void createClientSocket(Sensor sensor) {
        Socket socket = null;
        try {
            socket = new Socket(config.getInetAdress(), config.getPortNumber());
            socket.setSoTimeout(3*1000);
            SYSTEM_LOGGER.info(String.format("Sensor %s aktiviert", sensor.sensorArt ));

        } catch (IOException ex) {
            SYSTEM_LOGGER.error(ex.getMessage());
            shutdownSensor(socket, sensor);
            return;
        }
        createOutputStream(sensor, socket);
    }
        // Name ändern
    private static void createOutputStream(Sensor sensor, Socket socket) {
        try (OutputStream output = socket.getOutputStream()) {

            while (true) {
                String jsonString = outgoing.take();

                if (jsonString.equals(SHUTDOWN_KEY)){
                    writeToOutputStream(output, jsonString);
                    shutdownSensor(socket, sensor);

                }
                writeToOutputStream(output, jsonString);
            }
        } catch (IOException | InterruptedException ex) {
            SYSTEM_LOGGER.error(ex.getMessage());
            shutdownSensor(socket, sensor);
        }
    }

    private static void writeToOutputStream(OutputStream output, String jsonString)  {
        try {
            output.write(jsonString.getBytes());
            // Kommentar
            output.write("\r\n".getBytes());
            output.flush();
        } catch (IOException ex) {
            SYSTEM_LOGGER.error(ex.getMessage());
        }
    }

    private static void readFromInput (Scanner scanner){
        if(scanner.hasNext()) {
            String message = scanner.nextLine();
            if (validInputCheck(message)) {
                try {
                    outgoing.put(message);
                } catch (InterruptedException ex) {
                    SYSTEM_LOGGER.error(ex.getMessage());
                }
            }
        }
    }

    private static boolean validInputCheck (String input) {
        if (input.equals(SHUTDOWN_KEY))
            return true;

        // Neue Zeile einlesen, falls JSON Syntax Fehler
        Gson gson = new Gson();
        try {
            gson.fromJson(input, Ereignis.class);
            return true;
        } catch (JsonSyntaxException ex) {
            SYSTEM_LOGGER.error("JSON Syntax-Fehler --> Lese nächste Zeile");
            return false;
        }
    }

    private static void shutdownSensor(Socket socket, Sensor sensor){
        try {
            socket.close();
            SYSTEM_LOGGER.info(String.format("Client-Sensor %s geschlossen", sensor.sensorArt));
            System.exit(0);
        } catch (IOException ex) {
            SYSTEM_LOGGER.error(ex.getMessage());
        }
    }
}
