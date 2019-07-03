package fahrtenbuch;

import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.jms.*;
import javax.naming.NamingException;
import java.util.*;

public class Fahrtenbuch {

    private static final Logger LOGGER = Logger.getLogger(Fahrtenbuch.class);

    public static void main(String[] args) {

        MessagingServiceFahrtenbuch messagingServiceFahrtenbuch = new MessagingServiceFahrtenbuch();
        NachrichtenSpeicher nachrichtenSpeicher = new NachrichtenSpeicher();
        String messageId = null;
        Nachricht nachricht = null;
        Gson gson = new Gson();

        while (true) {
            try {
                messagingServiceFahrtenbuch.initialize();
                messagingServiceFahrtenbuch.connect();
                TextMessage messageFromEinheit = messagingServiceFahrtenbuch.subscribe();

                messageId = messageFromEinheit.getStringProperty("TelematikId");
                nachricht = gson.fromJson(messageFromEinheit.getText(), Nachricht.class);

                // Zur Map nachrichten hinzufügen
                nachrichtenSpeicher.addMessagesToList(messageId, nachricht);

                // Gefahrene Kilometer für jede Telematik-Einheit aktualisieren
                Set<String> keys = nachrichtenSpeicher.getNachrichten().keySet();
                for (String key : keys){
                    nachrichtenSpeicher.calculateTotalDistance(key);
                }

            } catch (JMSException | NamingException e) {
                // Im Fehlerfall loggen, aber weiterlaufen
                LOGGER.error(e.getMessage(), e);

            } finally {
                try {
                    messagingServiceFahrtenbuch.disconnect();
                } catch (JMSException e) {
                    // Im Fehlerfall loggen, aber weiterlaufen
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }
}
