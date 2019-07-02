package eingangsfilter;

import org.apache.log4j.Logger;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class EingangsFilter {

    private static final Logger LOGGER = Logger.getLogger(EingangsFilter.class);

    public static void main(String[] args) {
        MessagingService messagingService = new MessagingService();

        while (true) {
            try {
                messagingService.initialize();
                messagingService.connect();
                Message msg = messagingService.receive();
                messagingService.publish(msg);

            } catch (NamingException | JMSException e) {
                // Im Fehlerfall loggen, aber weiterlaufen
                LOGGER.error(e.getMessage(), e);

            } finally {
                try {
                    messagingService.disconnect();
                } catch (JMSException e) {
                    // Im Fehlerfall loggen, aber weiterlaufen
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }
}
