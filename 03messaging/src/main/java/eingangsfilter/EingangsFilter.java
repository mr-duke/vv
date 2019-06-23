package eingangsfilter;

import org.apache.log4j.Logger;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class EingangsFilter {

    private static final Logger LOGGER = Logger.getLogger(EingangsFilter.class);
    private static final String QUEUE_NAME = "dynamicQueues/fahrdaten";
    private static final String TOPIC_NAME = "verteiler";

    private static Properties props;
    private static Context ctx;
    private static ConnectionFactory connectionFactory;
    private static Queue source;
    private static Topic destination;


    public static void main(String[] args) {
        initialize();
        receive();
    }


    public static void initialize () {
        props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(
                Context.PROVIDER_URL, "tcp://localhost:61616");

        try {
            ctx = new InitialContext(props);
            connectionFactory = (ConnectionFactory) ctx.lookup("ConnectionFactory");
            source = (Queue) ctx.lookup(QUEUE_NAME);
        } catch (NamingException e) {
            LOGGER.error(e.getMessage());
        }
    }


    public static void receive (){

        try {
            Connection connection = connectionFactory.createConnection();
            connection.start();

            Session session =
                    connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            MessageConsumer consumer =
                    session.createConsumer(source);

            while (true) {
                TextMessage message = (TextMessage) consumer.receive(0);
                LOGGER.info(message);
                publish(message);
            }

        } catch (JMSException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public static void publish(TextMessage message) {

        try {
            Connection connection = connectionFactory.createConnection();
            connection.start();

            Session session =
                    connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            destination = (Topic) session.createTopic(TOPIC_NAME);

            MessageProducer publisher =
                    session.createProducer(destination);
            publisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            //TextMessage msg = session.createTextMessage(message);
            publisher.send(message);

            publisher.close();
            session.close();
            connection.stop();
            connection.close();

        } catch (JMSException e){
            LOGGER.error(e.getMessage());
        }
    }
}
