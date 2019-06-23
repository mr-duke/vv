import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

public class Publisher {

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


        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session =
                connection.createSession(false,Session.AUTO_ACKNOWLEDGE);

        Topic destination = (Topic) session.createTopic("charlystopic");

        MessageProducer producer =
                session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        for (int i = 0; i < 100000 ; i++) {
            Thread.sleep(5000);
            TextMessage message =
                    session.createTextMessage("Servus Charly, VV ist super!");
            producer.send(message);
        }

        producer.close(); session.close();
        connection.stop(); connection.close();
    }
}
