package fahrtenbuch;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

/* Drei ähnliche Klassen "MessagingService...", allerdings mit leicht unterschiedlichen Funktionen
 * Weitere Annahme von Karl Herzog: Die drei Packages laufen getrennt voneinander auf unterschiedlichen Systemen und nicht im selben Projekt,
 * daher absichtlich nicht als eine gemeinsame Utility-Klasse konzipiert */
public class MessagingServiceFahrtenbuch {

    private static final String TOPIC_NAME = "verteiler";

    private Connection connection;
    private Session session;
    private MessageConsumer subscriber;


    public void initialize() throws NamingException, JMSException {
        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(
                Context.PROVIDER_URL,"tcp://localhost:61616");

        Context ctx = new InitialContext(props);
        ConnectionFactory connectionFactory = (ConnectionFactory) ctx.lookup("ConnectionFactory");;
        connection = connectionFactory.createConnection();
        connection.setClientID("fahrtenbuch");

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic source = (Topic) session.createTopic(TOPIC_NAME);
        // Erzeuge DurableSubscriber
        subscriber = session.createDurableSubscriber(source, "fahrtenbuch");
    }

    public void connect() throws JMSException {
        connection.start();
    }

    public TextMessage subscribe() throws JMSException {
        TextMessage messageFromEinheit = (TextMessage) subscriber.receive(0);
        return messageFromEinheit;
    }

    public void disconnect() throws JMSException{
        subscriber.close();
        session.close();
        connection.stop();
        connection.close();
    }
}
