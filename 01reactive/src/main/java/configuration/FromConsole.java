package configuration;

import de.thro.inf.reactive.Sensor;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

public class FromConsole implements IConfiguration {

    private static final Logger SYSTEM_LOGGER = Logger.getLogger("systemLogger");
    private static final Logger EVENTS_LOGGER = Logger.getLogger("eventLogger");

    private Properties prop = null;

    @Override
    public void loadPropertiesFile() {
        prop = new Properties();
        try (InputStream input = FromConsole.class.getClassLoader().getResourceAsStream("fromConsole.properties")){
            prop.load(input);

        } catch (IOException ex) {
            SYSTEM_LOGGER.error(ex.getMessage());
        } ;
    }

    @Override
    public Sensor.SensorArt getSensorArt() {
        loadPropertiesFile();
        return Sensor.SensorArt.valueOf(prop.getProperty("sensorArt"));
    }

    @Override
    public InetAddress getInetAdress() {
        loadPropertiesFile();
        InetAddress inetAddress = null;
        try {
           inetAddress = InetAddress.getByName(prop.getProperty("inetAddress"));
        } catch (UnknownHostException ex) {
           SYSTEM_LOGGER.error(ex.getMessage());
        }
        return inetAddress;
    }

    @Override
    public int getPortNumber() {
        loadPropertiesFile();
        int port = Integer.parseInt(prop.getProperty("port"));
        return port;
    }
}
