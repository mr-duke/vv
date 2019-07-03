package telematik;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

/* Drei ähnliche Klassen "MessagingService...", allerdings mit leicht unterschiedlichen Funktionen
 * Weitere Annahme von Karl Herzog: Die drei Packages laufen getrennt voneinander auf unterschiedlichen Systemen und nicht im selben Projekt,
 * daher absichtlich nicht als eine gemeinsame Utility-Klasse konzipiert */
public class MessagingServiceTelematik {

    private final String QUEUE_NAME = "dynamicQueues/fahrdaten";

    private Connection connection;
    private Session session;
    private MessageProducer producer;


    public void initialize() throws NamingException, JMSException {
        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(
                Context.PROVIDER_URL,"tcp://localhost:61616");

        Context ctx = new InitialContext(props);
        ConnectionFactory connectionFactory = (ConnectionFactory) ctx.lookup("ConnectionFactory");;
        Queue destination = (Queue) ctx.lookup(QUEUE_NAME);

        connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
    }

    public void connect() throws JMSException {
        connection.start();
    }

    public void send (String msg, String telematikId, boolean isAlarm) throws JMSException {
        TextMessage textMessage = session.createTextMessage(msg);
        textMessage.setStringProperty("TelematikId", telematikId);
        textMessage.setBooleanProperty("Alarm", isAlarm);

        producer.send(textMessage);
    }

    public void disconnect() throws JMSException{
        producer.close();
        session.close();
        connection.stop();
        connection.close();
    }
}
