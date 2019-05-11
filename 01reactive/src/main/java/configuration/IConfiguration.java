package configuration;

import de.thro.inf.reactive.Sensor;

import java.net.InetAddress;

public interface IConfiguration {

    public void loadPropertiesFile();

    public Sensor.SensorArt getSensorArt();

    public InetAddress getInetAdress();

    public int getPortNumber();
}
