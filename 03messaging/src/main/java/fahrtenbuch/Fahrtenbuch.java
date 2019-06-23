package fahrtenbuch;

import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class Fahrtenbuch {

    private static final Logger LOGGER = Logger.getLogger(Fahrtenbuch.class);
    private static final String TOPIC_NAME = "verteiler";

    public static List<Nachricht> nachrichtenEinheit1 = new LinkedList<>();
    public static List<Nachricht> nachrichtenEinheit2 = new LinkedList<>();
    public static List<Nachricht> nachrichtenEinheit3 = new LinkedList<>();
    public static List<Nachricht> nachrichtenEinheit4 = new LinkedList<>();


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
            connection.start();

            Session session =
                    connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            Topic source = (Topic) session.createTopic(TOPIC_NAME);

            String selectorEinheit1 = "(TelematikId= '1')";
            String selectorEinheit2 = "(TelematikId= '2')";
            String selectorEinheit3 = "(TelematikId= '3')";
            String selectorEinheit4 = "(TelematikId= '4')";

            MessageConsumer consumer1 =
                    session.createConsumer(source, selectorEinheit1);
            MessageConsumer consumer2 =
                    session.createConsumer(source, selectorEinheit2);
            MessageConsumer consumer3 =
                    session.createConsumer(source, selectorEinheit3);
            MessageConsumer consumer4 =
                    session.createConsumer(source, selectorEinheit4);

            Gson gson = new Gson();

            // TO DO
            // Durable Subscriber
            // statt while true for-Schleife

            while (true) {
                TextMessage messageEinheit1 = (TextMessage) consumer1.receive(0);
                Nachricht einheit1 = gson.fromJson(messageEinheit1.getText(), Nachricht.class);
                nachrichtenEinheit1.add(einheit1);

                TextMessage messageEinheit2 = (TextMessage) consumer2.receive(0);
                Nachricht einheit2 = gson.fromJson(messageEinheit2.getText(), Nachricht.class);
                nachrichtenEinheit2.add(einheit2);

                TextMessage messageEinheit3 = (TextMessage) consumer3.receive(0);
                Nachricht einheit3 = gson.fromJson(messageEinheit3.getText(), Nachricht.class);
                nachrichtenEinheit3.add(einheit3);

                TextMessage messageEinheit4 = (TextMessage) consumer4.receive(0);
                Nachricht einheit4 = gson.fromJson(messageEinheit4.getText(), Nachricht.class);
                nachrichtenEinheit4.add(einheit4);

                /*LOGGER.info("Einheit 1: ");
                for (Nachricht nachricht : nachrichtenEinheit1) {
                    LOGGER.info(nachricht.toString());
                }

                LOGGER.info("Einheit 2: ");
                for (Nachricht nachricht : nachrichtenEinheit2) {
                    LOGGER.info(nachricht.toString());
                }

                LOGGER.info("Einheit 3: ");
                for (Nachricht nachricht : nachrichtenEinheit3) {
                    LOGGER.info(nachricht.toString());
                }

                LOGGER.info("Einheit 4: ");
                for (Nachricht nachricht : nachrichtenEinheit4) {
                    LOGGER.info(nachricht.toString());
                }*/

            }
        } catch (JMSException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
