import javax.naming.Context;
import java.util.Properties;
import javax.jms.*;
import javax.naming.InitialContext;

public class Sender {

    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(
                Context.PROVIDER_URL,"tcp://localhost:61616");

        Context ctx = new InitialContext(props);
        ConnectionFactory connectionFactory =
                (ConnectionFactory) ctx.lookup("ConnectionFactory");

        Queue destination = (Queue) ctx.lookup("dynamicQueues/charlysqueue");

        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session =
                connection.createSession(false,Session.AUTO_ACKNOWLEDGE);

        MessageProducer producer =
                session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        TextMessage message =
                session.createTextMessage("Servus Charly, VV ist super!");
        producer.send(message);

        producer.close(); session.close();
        connection.stop(); connection.close();
    }
}
