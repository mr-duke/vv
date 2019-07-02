package eingangsfilter;

import org.apache.log4j.Logger;
import javax.jms.*;
import javax.naming.NamingException;

public class EingangsFilter {

    private static final Logger LOGGER = Logger.getLogger(EingangsFilter.class);

    public static void main(String[] args) {
        MessagingServiceFilter messagingServiceFilter = new MessagingServiceFilter();

        while (true) {
            try {
                messagingServiceFilter.initialize();
                messagingServiceFilter.connect();
                Message msg = messagingServiceFilter.receive();
                messagingServiceFilter.publish(msg);

            } catch (NamingException | JMSException e) {
                // Im Fehlerfall loggen, aber weiterlaufen
                LOGGER.error(e.getMessage(), e);

            } finally {
                try {
                    messagingServiceFilter.disconnect();
                } catch (JMSException e) {
                    // Im Fehlerfall loggen, aber weiterlaufen
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }
}
