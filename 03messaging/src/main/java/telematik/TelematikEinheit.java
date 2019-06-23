package telematik;

import org.apache.log4j.Logger;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

/* Starten mehrerer Telematikeinheiten in IntelliJ:
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
    private static final String QUEUE_NAME = "dynamicQueues/fahrdaten";

    private long id;

    TelematikEinheit(long id) {
        this.id = id;
        LOGGER.info(String.format("Neue Telematikeinheit mit ID %d angelegt", this.id));
    }

    TelematikEinheit(){}

    public static void main(String[] args) {

        TelematikEinheit einheit = new TelematikEinheit(Long.parseLong(args[0]));
        NachrichtenGenerator generator = new NachrichtenGenerator(einheit.getId());

        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(
                Context.PROVIDER_URL,"tcp://localhost:61616");

        Context ctx = null;
        ConnectionFactory connectionFactory = null;
        Queue destination = null;
        try {
            ctx = new InitialContext(props);
            connectionFactory = (ConnectionFactory) ctx.lookup("ConnectionFactory");
            destination = (Queue) ctx.lookup(QUEUE_NAME);
        } catch (NamingException e) {
            LOGGER.error(e.getMessage());
        }

        while (true) {
            try {
                Connection connection = connectionFactory.createConnection();
                connection.start();
                LOGGER.info(String.format("telematik.TelematikEinheit %d hat Verbindung aufgebaut", einheit.getId()));

                Session session =
                        connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                MessageProducer producer =
                        session.createProducer(destination);
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

                TextMessage message =
                        session.createTextMessage(generator.generateNachricht());
                message.setStringProperty("TelematikId", String.valueOf(einheit.getId()));

                producer.send(message);

                producer.close();
                session.close();
                connection.stop();
                connection.close();
                LOGGER.info(String.format("telematik.TelematikEinheit %d hat Verbindung beendet", einheit.getId()));

                Thread.sleep(TIME_INTERVALL_SEND);

            } catch (JMSException e) {
                LOGGER.error(e.getMessage());
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    public long getId() {
        return id;
    }
}
