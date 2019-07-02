package eingangsfilter;

import org.apache.log4j.Logger;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

/* Drei ähnliche Klassen "MessagingService...", allerdings mit leicht unterschiedlichen Funktionen
 * Weitere Annahme von Karl Herzog: Die drei Packages laufen getrennt voneinander auf unterschiedlichen Systemen und nicht im selben Projekt,
 * daher absichtlich nicht als eine gemeinsame Utility-Klasse konzipiert */
public class MessagingServiceFilter {

    private static final Logger LOGGER = Logger.getLogger(MessagingServiceFilter.class);
    private final String QUEUE_NAME = "dynamicQueues/fahrdaten";
    private final String TOPIC_NAME = "verteiler";
    private final String TOPIC_NAME_ALARM = "alarme";

    private Connection connection;
    private Session session;
    private MessageConsumer consumer;
    private MessageProducer publisher;
    private Queue source;
    private Topic destination;

    public void initialize() throws NamingException, JMSException {
        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(
                Context.PROVIDER_URL,"tcp://localhost:61616");

        Context ctx = new InitialContext(props);
        ConnectionFactory connectionFactory = (ConnectionFactory) ctx.lookup("ConnectionFactory");;
        source = (Queue) ctx.lookup(QUEUE_NAME);
        connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        consumer = session.createConsumer(source);
    }

    public void connect() throws JMSException {
        connection.start();
    }

    public Message receive() throws JMSException {
        TextMessage message = (TextMessage) consumer.receive(0);
        LOGGER.info(message.getText());
        return message;
    }

    public void publish(Message message) throws JMSException {
        boolean isAlarm = message.getBooleanProperty("Alarm");

        if (isAlarm){
            destination = (Topic) session.createTopic(TOPIC_NAME_ALARM);
        } else {
            destination = (Topic) session.createTopic(TOPIC_NAME);
        }

        publisher = session.createProducer(destination);
        publisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        publisher.send(message);
    }

    public void disconnect() throws JMSException{
        publisher.close();
        session.close();
        connection.stop();
        connection.close();
    }
}
