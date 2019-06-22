import org.apache.log4j.Logger;

import java.util.Arrays;

public class LogTest {

     private static final Logger LOGGER = Logger.getLogger(LogTest.class);

    public static void main(String[] args) {

        /*LOGGER.debug("This is debug message");

        LOGGER.info("This is info message");

        LOGGER.warn("This is warn message");

        LOGGER.fatal("This is fatal message");

        LOGGER.error("This is error message");

        System.out.println("Logic executed successfully....");*/

        TelematikEinheit t1 = new TelematikEinheit();
        TelematikEinheit t2 = new TelematikEinheit();
        TelematikEinheit t3 = new TelematikEinheit();
        TelematikEinheit t4 = new TelematikEinheit();
        TelematikEinheit t5 = new TelematikEinheit();

        LOGGER.info(Arrays.toString(t1.gpsSimulator()));
        LOGGER.info(Arrays.toString(t1.gpsSimulator()));
        LOGGER.info(Arrays.toString(t1.gpsSimulator()));
        LOGGER.info(Arrays.toString(t1.gpsSimulator()));
        LOGGER.info(Arrays.toString(t1.gpsSimulator()));


    }
}
