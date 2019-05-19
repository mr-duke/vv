package configuration;

import de.thro.inf.reactive.Sensor;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.util.Properties;

// Lese Eingaben des Sensors aus File aus
// Konfiguration durch Properties-File "fromFile.properties"
public class FromFile implements IConfiguration {

    private static final Logger SYSTEM_LOGGER = Logger.getLogger("systemLogger");
    private final String FROM_FILE_PROPERTIES_KEY = "fromFile.properties";
    private final String SENSOR_ART_PROPERTY_KEY =  "sensorArt";
    private final String INET_ADDR_PROPERTY_KEY = "inetAddress";
    private final String PORT_PROPERTY_KEY = "port";
    private final String FILE_NAME_PROPERTY_KEY = "fileName";

    private Properties prop = null;
    private InputStream INPUT_STREAM = null;

    public FromFile() {
        try {
            INPUT_STREAM = new FileInputStream(provideFile());
        } catch (FileNotFoundException ex) {
            SYSTEM_LOGGER.error(ex.getMessage());
        }
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

    private void loadPropertiesFile() {
        prop = new Properties();
        try (InputStream input = FromConsole.class.getClassLoader().getResourceAsStream(FROM_FILE_PROPERTIES_KEY)){
            prop.load(input);

        } catch (IOException ex) {
            SYSTEM_LOGGER.error(ex.getMessage());
        } ;
    }

    // Suche nach File-Namen im Properties-File und erzeuge daraus File für den InputStream
    private File provideFile(){
        loadPropertiesFile();
        String fileName = prop.getProperty(FILE_NAME_PROPERTY_KEY);
        URL fileLocation = getClass().getClassLoader().getResource(fileName);
        File file = null;
        {
            try {
                file = Paths.get(fileLocation.toURI()).toFile();
            } catch (URISyntaxException ex) {
                SYSTEM_LOGGER.error(ex.getMessage());
            }
        }
        return file;
    }
}
