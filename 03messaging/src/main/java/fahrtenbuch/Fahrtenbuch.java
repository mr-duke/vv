package fahrtenbuch;

import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.*;

public class Fahrtenbuch {

    private static final Logger LOGGER = Logger.getLogger(Fahrtenbuch.class);
    private static final String TOPIC_NAME = "verteiler";

    // Key = ID der Telematik-Einheit
    // Value = Liste aller Nachrichten der jeweiligen Einheit
    public static Map<String, List<Nachricht>> nachrichten = new HashMap<> ();


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
            connection.setClientID("fahrtenbuch");
            connection.start();

            Session session =
                    connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            Topic source = (Topic) session.createTopic(TOPIC_NAME);

            MessageConsumer consumer =
                    session.createDurableSubscriber(source, "fahrtenbuch");

            Gson gson = new Gson();

            while (true) {
                TextMessage messageEinheit = (TextMessage) consumer.receive(0);
                String messageId = messageEinheit.getStringProperty("TelematikId");
                Nachricht nachricht = gson.fromJson(messageEinheit.getText(), Nachricht.class);
                addMessagesToList(messageId, nachricht);

                LOGGER.info("Einheit 1");
                List<Nachricht> nachrichtenEinheit1 = nachrichten.getOrDefault("1", null);
                if (nachrichtenEinheit1 != null){
                    for (Nachricht n : nachrichtenEinheit1) {
                        LOGGER.info(n.toString());
                    }
                }

                LOGGER.info("Einheit 2");
                List<Nachricht> nachrichtenEinheit2 = nachrichten.getOrDefault("2", null);
                if (nachrichtenEinheit2 != null){
                    for (Nachricht n : nachrichtenEinheit2) {
                        LOGGER.info(n.toString());
                    }
                }

                LOGGER.info("Einheit 3");
                List<Nachricht> nachrichtenEinheit3 = nachrichten.getOrDefault("3", null);
                if (nachrichtenEinheit3 != null){
                    for (Nachricht n : nachrichtenEinheit3) {
                        LOGGER.info(n.toString());
                    }
                }


            }
        } catch (JMSException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public static void addMessagesToList(String key, Nachricht nachricht) {
        List<Nachricht> nachrichtenAsList = nachrichten.getOrDefault(key, null);

        // Falls noch keine List<Nachricht> für Key (=TelematikId) vorhanden, lege neue Liste an und füge erste Nachricht dazu
        // Dann in Map unter entsprechendem Key abspeichern
        if (nachrichtenAsList == null){
            List<Nachricht> tempList = new LinkedList<>();
            tempList.add(nachricht);
            nachrichten.put(key,tempList);
        } else {
            // FalLs bereits Liste an Nachrichten vorhanden, füge neue Nachricht hinzu
            // Dann in Map unter entsprechendem Key abspeichern
            nachrichtenAsList.add(nachricht);
            nachrichten.put(key, nachrichtenAsList);
        }
    }
}
