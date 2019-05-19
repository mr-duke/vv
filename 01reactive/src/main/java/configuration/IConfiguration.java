package configuration;

import de.thro.inf.reactive.Sensor;

import java.io.InputStream;
import java.net.InetAddress;

public interface IConfiguration {

    Sensor.SensorArt getSensorArt();

    InetAddress getInetAdress();

    int getPortNumber();

    InputStream getInputStream();
}
