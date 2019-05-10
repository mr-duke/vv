package de.thro.inf.reactive;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Sensor {

    private SensorArt sensorArt;

    public enum SensorArt {LINKS, RECHTS};
    public Sensor (SensorArt sensorArt) {
        this.sensorArt = sensorArt;
    }

    private static final Logger SYSTEM_LOGGER = Logger.getLogger("systemLogger");
    private static final Logger EVENTS_LOGGER = Logger.getLogger("eventLogger");

    public static void main(String[] args) {
        Sensor sensor = new Sensor(SensorArt.RECHTS);
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
            //while (true) {
                String jsonString = "{\"mitarbeiterId\" : \"11-01-02-0a-0b-0c\", \"richtung\" : \"LINKS\"}";
                output.write(jsonString.getBytes());
                output.flush();

            //}
        } catch (IOException e) {
            e.printStackTrace();
        }
        closeSocket(socket,sensor);
    }

    public static void closeSocket (Socket socket, Sensor sensor){
        try {
            socket.close();
            SYSTEM_LOGGER.info(String.format("Client-Sensor %s geschlossen", sensor.sensorArt));
        } catch (IOException ex) {
            SYSTEM_LOGGER.error(ex.getMessage());
        }
    }
}
