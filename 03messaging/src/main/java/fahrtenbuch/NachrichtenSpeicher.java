package fahrtenbuch;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NachrichtenSpeicher {

    private static final Logger LOGGER = Logger.getLogger(NachrichtenSpeicher.class);

    // Key = ID der Telematik-Einheit
    // Value = Liste aller Nachrichten der jeweiligen Einheit
    private Map<String, List<Nachricht>> nachrichten = new HashMap<>();


    public void addMessagesToList(String telematikId, Nachricht nachricht) {
        List<Nachricht> nachrichtenAsList = nachrichten.getOrDefault(telematikId, null);

        // Falls noch keine List<Nachricht> für Key (= telematikId) vorhanden, lege neue Liste an und füge erste Nachricht dazu
        // Dann in Map unter entsprechendem Key abspeichern
        if (nachrichtenAsList == null){
            List<Nachricht> tempList = new LinkedList<>();
            tempList.add(nachricht);
            nachrichten.put(telematikId,tempList);
        } else {
            // Falls bereits Liste an Nachrichten vorhanden, füge neue Nachricht hinzu
            // Dann in Map unter entsprechendem Key abspeichern
            nachrichtenAsList.add(nachricht);
            nachrichten.put(telematikId, nachrichtenAsList);
        }
    }

    public void calculateTotalDistance(String telematikId){
        List<Nachricht> nachrichtenAsList = nachrichten.getOrDefault(telematikId, null);

        // Falls List<Nachricht> für Key (= telematikId) noch leer, zurückfahrene Kilometer == 0
        if (nachrichtenAsList.size() == 0){
            LOGGER.info(String.format("Einheit %s: Gesamtkilometer 0", telematikId));
        } else {
            int gefahrenInKM = 0;
            for (Nachricht n : nachrichtenAsList){
                gefahrenInKM += n.getStreckeGefahren();
            }
            LOGGER.info(String.format("Einheit %s: Gesamtkilometer %d", telematikId, gefahrenInKM));
        }
    }

    public Map<String, List<Nachricht>> getNachrichten() {
        return nachrichten;
    }
}
