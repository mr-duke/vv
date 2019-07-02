package telematik;

import org.apache.log4j.Logger;

import javax.jms.*;
import javax.naming.NamingException;
import java.util.Random;

/* Kommentar: Karl Herzog
 * Starten mehrerer Telematikeinheiten in IntelliJ:
 *  1. Run -> Edit Configurations
 *  2. Bestehende telematik.TelematikEinheit duplizieren oder neu anlegen mit Main class: telematik.TelematikEinheit
 *  3. Program arguments: individuelle ID vergeben
 *  4. Apply + OK
 *  5. Run -> Run ...
 * */

public class TelematikEinheit {

    private static final Logger LOGGER = Logger.getLogger(TelematikEinheit.class);
    // Sendeintervall der Nachrichten in Millisekunden
    private static final long TIME_INTERVALL_SEND = 5000 ;
    private long id;

    TelematikEinheit(long id) {
        this.id = id;
        LOGGER.info(String.format("Neue Telematikeinheit mit ID %d angelegt", this.id));
    }

    TelematikEinheit(){}

    public static void main(String[] args) {

        TelematikEinheit einheit = new TelematikEinheit(Long.parseLong(args[0]));
        NachrichtenGenerator generator = new NachrichtenGenerator(einheit.getId());
        MessagingService messagingService = new MessagingService();

        // Timer setzen für später
        long startTime = System.currentTimeMillis();
        // Zufälliges Zeitintervall zwischen 15 und 25 Sekunden für Simulation der Alarmnachricht in Millisec
        Random random = new Random();
        long timeToAlarm = (15 + random.nextInt(11)) * 1000;

        while (true) {
            try {
                messagingService.initialize();
                messagingService.connect();
                LOGGER.info(String.format("TelematikEinheit %d hat Verbindung aufgebaut", einheit.getId()));

                String msg = generator.generateNachricht();
                boolean isAlarm;
                if (System.currentTimeMillis() - startTime >= timeToAlarm ) {
                    isAlarm = true;
                } else {
                    isAlarm = false;
                }

                messagingService.publish(msg, String.valueOf(einheit.getId()), isAlarm);
                if (isAlarm) {
                    LOGGER.fatal(String.format("TelematikEinheit %d hat Alarm gesendet!", einheit.getId()));
                    // Telematik-Einheit im Alarmfall herunterfahren. Ansonsten schicke weiterhin Nachrichten
                    return;
                }

            } catch (JMSException | NamingException e) {
                // Im Fehlerfall loggen, aber weiterlaufen
                LOGGER.error(e.getMessage(), e);

            } finally {
                // Am Ende jedes Durchlaufs Verbindung schließen
                try {
                    messagingService.disconnect();
                    LOGGER.info(String.format("TelematikEinheit %d hat Verbindung beendet", einheit.getId()));
                    
                    Thread.sleep(TIME_INTERVALL_SEND);

                } catch (JMSException | InterruptedException e) {
                    // Im Fehlerfall loggen und Telematik-Einheit herunterfahren
                    LOGGER.error(e.getMessage(), e);
                    return;
                }
            }
        }
    }

    private long getId() {
        return id;
    }
}
