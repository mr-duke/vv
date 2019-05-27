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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Sensor {

    private static final Logger SYSTEM_LOGGER = Logger.getLogger("systemLogger");
    private static final String SHUTDOWN_KEY = "QUIT";
    private static BlockingQueue<String> outgoing = new LinkedBlockingQueue<>();
    private static Executor exec = Executors.newCachedThreadPool();

    // Hier wird Config-Art festgelegt: new FromConsole() oder new FromFile()
    // Config liest mithilfe von Properties-File die nötigen Konfigurationsinfos aus
    private static IConfiguration config = new FromConsole();

    private SensorArt sensorArt;
    private Sensor(SensorArt sensorArt) {

        this.sensorArt = sensorArt;
    }
    public enum SensorArt {LINKS, RECHTS};

    public static void main(String[] args) {
        Sensor sensor = new Sensor(config.getSensorArt());
        exec.execute(() -> provideClientSocket(sensor));

        Scanner scanner = new Scanner(config.getInputStream());
        while (true){
            readFromInputStream(scanner);
        }
    }

    private static void provideClientSocket(Sensor sensor) {
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
        provideOutputStream(sensor, socket);
    }

    private static void provideOutputStream(Sensor sensor, Socket socket) {
        try (OutputStream output = socket.getOutputStream()) {

            while (true) {
                String jsonString = outgoing.take();
                // Bei Senden von SHUTDOWN_KEY:
                // Fahre Sensor/Client herunter und sende Info auch an Server zum Entfernen des Client-Socket
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
            // Nötig, damit Scanner in Server-Objekt das Zeilenende erkennt
            output.write("\r\n".getBytes());
            output.flush();
        } catch (IOException ex) {
            SYSTEM_LOGGER.error(ex.getMessage());
        }
    }

    // Lies Eingabe (entweder aus Konsole oder aus File) und übergebe es der BlockingQueue
    private static void readFromInputStream(Scanner scanner){
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

    // Prüfen der Eingabe nach gewissen Kriterien zur Sicherstellung der Robustheit des Systems
    private static boolean validInputCheck (String input) {
        // SHUTDOWN_KEY "QUIT" soll an Server weitergereicht werden als Signal, den Client-Socket geordnet zu schließen
        if (input.equals(SHUTDOWN_KEY))
            return true;

        // Falls keine korrekte Angabe des Sensorrichtung ("LINKS" oder "RECHTS"), return false und warte auf nächste Zeile
        if (!input.contains("LINKS") && !input.contains("RECHTS")){
            SYSTEM_LOGGER.error("Ungültige Eingabe der Sensorrichtung --> Lese nächste Zeile");
            return false;
        }
        // Falls JSON Syntax Fehler, return false und warte danach auf nächste Zeile:
        // Eingabe wird erst gar nicht an BlockingQueue bzw. Server weitergereicht
        // Falls JSON Syntax korrekt, return true und reiche danach input an BlockingQueue weiter
        Gson gson = new Gson();
        try {
            gson.fromJson(input, Ereignis.class);
            return true;
        } catch (JsonSyntaxException ex) {
            SYSTEM_LOGGER.error("JSON Syntax-Fehler --> Lese nächste Zeile");
            return false;
        }
    }

    // Geordnetes Herunterfahren des Sensors/Clients, sodass Server nicht abstürzt
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
