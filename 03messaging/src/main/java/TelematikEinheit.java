import org.apache.log4j.Logger;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.Random;

public class TelematikEinheit {

    private static final Logger LOGGER = Logger.getLogger(TelematikEinheit.class);
    // Sendeintervall der Nachrichten in Millisekunden
    private static final long TIME_INTERVALL_SEND = 5000 ;
    private static final String QUEUE_NAME = "dynamicQueues/fahrdaten";

    private long id;
    // Statischer Zähler zum Erzeugen eindeutiger IDs beim Erzeugen eines neuen Objekts im Konstruktor
    public static long idCounter = 1;

    TelematikEinheit() {
        this.id = idCounter;
        idCounter++ ;
        LOGGER.info(String.format("Neue Telematikeinheit mit ID %d angelegt", this.id));
    }

    public long getId() {
        return id;
    }

    public static void main(String[] args) {

        TelematikEinheit einheit = new TelematikEinheit();
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

        try {
            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session =
                    connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            MessageProducer producer =
                    session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            TextMessage message =
                    session.createTextMessage(generator.generateNachricht());

            producer.send(message);

            producer.close();
            session.close();
            connection.stop();
            connection.close();
            LOGGER.info(String.format("Verbindung mit TelematikEinheit %d wurde beendet", einheit.getId()));
        } catch (JMSException e) {
            LOGGER.error(e.getMessage());
        }

    }



}
