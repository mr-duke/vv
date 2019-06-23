import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

public class Receiver {

    // WICHTIG für mündliche Prüfung:
    // 1. Publisher Subscriber Pattern: asynchrones Observer-Pattern
    // 2. DurableSubriber: Topic behält telematik.NachrichtDTO solange auf, bis alle subscriber angemeldet
    // und telematik.NachrichtDTO abgerufen haben

    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(
                Context.PROVIDER_URL,"tcp://localhost:61616");

        Context ctx = new InitialContext(props);
        ConnectionFactory connectionFactory =
                (ConnectionFactory) ctx.lookup("ConnectionFactory");

        Queue source = (Queue) ctx.lookup("dynamicQueues/charlysqueue");

        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session =
                connection.createSession(false,Session.AUTO_ACKNOWLEDGE);

        MessageConsumer consumer =
                session.createConsumer(source);

        TextMessage message = (TextMessage) consumer.receive();

        System.out.println("Empfangen:" + message);

        consumer.close(); session.close();
        connection.stop(); connection.close();
    }
}
