package telematik;

import org.apache.log4j.Logger;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class MessagingService {

    private static final Logger LOGGER = Logger.getLogger(MessagingService.class);
    private static final String QUEUE_NAME = "dynamicQueues/fahrdaten";

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

    public void publish(String msg, String telematikId, boolean isAlarm) throws JMSException {
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
