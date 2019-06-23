package fahrtenbuch;

import org.apache.log4j.Logger;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class Fahrtenbuch {

    private static final Logger LOGGER = Logger.getLogger(Fahrtenbuch.class);
    private static final String TOPIC_NAME = "verteiler";

    public static void main(String[] args) {
        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(
                Context.PROVIDER_URL,"tcp://localhost:61616");

        Context ctx = null;
        ConnectionFactory connectionFactory = null;

        try {
            ctx = new InitialContext(props);
            connectionFactory = (ConnectionFactory) ctx.lookup("ConnectionFactory");
        } catch (NamingException e) {
            LOGGER.error(e.getMessage());
        }

        try {
            Connection connection = connectionFactory.createConnection();
            connection.setClientID("subscriber");
            connection.start();

            Session session =
                    connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            Topic source = (Topic) session.createTopic(TOPIC_NAME);

            MessageConsumer consumer =
                    session.createDurableSubscriber(source, "subscriber");

            while (true) {
                TextMessage message = (TextMessage) consumer.receive(0);
                LOGGER.info(message);
            }
        } catch (JMSException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
