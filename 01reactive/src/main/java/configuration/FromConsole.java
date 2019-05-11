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
    private final String FROM_FILE_PROPERTIES_KEY = "fromConsole.properties";
    private final String SENSOR_ART_PROPERTY_KEY =  "sensorArt";
    private final String INET_ADDR_PROPERTY_KEY = "inetAddress";
    private final String PORT_PROPERTY_KEY = "port";

    private Properties prop = null;
    private final InputStream INPUT_STREAM = System.in;

    @Override
    public void loadPropertiesFile() {
        prop = new Properties();
        try (InputStream input = FromConsole.class.getClassLoader().getResourceAsStream(FROM_FILE_PROPERTIES_KEY)){
            prop.load(input);

        } catch (IOException ex) {
            SYSTEM_LOGGER.error(ex.getMessage());
        } ;
    }

    @Override
    public Sensor.SensorArt getSensorArt() {
        loadPropertiesFile();
        return Sensor.SensorArt.valueOf(prop.getProperty(SENSOR_ART_PROPERTY_KEY));
    }

    @Override
    public InetAddress getInetAdress() {
        loadPropertiesFile();
        InetAddress inetAddress = null;
        try {
           inetAddress = InetAddress.getByName(prop.getProperty(INET_ADDR_PROPERTY_KEY));
        } catch (UnknownHostException ex) {
           SYSTEM_LOGGER.error(ex.getMessage());
        }
        return inetAddress;
    }

    @Override
    public int getPortNumber() {
        loadPropertiesFile();
        int port = Integer.parseInt(prop.getProperty(PORT_PROPERTY_KEY));
        return port;
    }

    @Override
    public InputStream getInputStream() {
        return INPUT_STREAM;
    }
}
