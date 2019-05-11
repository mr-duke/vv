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

public class FromFile implements IConfiguration {

    private static final Logger SYSTEM_LOGGER = Logger.getLogger("systemLogger");
    private static final Logger EVENTS_LOGGER = Logger.getLogger("eventLogger");

    private Properties prop = null;
    private InputStream INPUT_STREAM = null;

    public FromFile() {
        try {
            INPUT_STREAM = new FileInputStream(createFile());
        } catch (FileNotFoundException ex) {
            SYSTEM_LOGGER.error(ex.getMessage());
        }
    }

    @Override
    public void loadPropertiesFile() {
        prop = new Properties();
        try (InputStream input = FromConsole.class.getClassLoader().getResourceAsStream("fromFile.properties")){
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

    @Override
    public InputStream getInputStream() {
        return INPUT_STREAM;
    }


    private File createFile(){
        loadPropertiesFile();
        String fileName = prop.getProperty("fileName");
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
