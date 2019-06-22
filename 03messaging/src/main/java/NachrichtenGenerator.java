import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public class NachrichtenGenerator {

    // ID der TelematikEinheit, für die GPS und Fahrdaten simuliert werden sollen
    private long telematikId;

    public NachrichtenGenerator(long telematikId) {
        this.telematikId = telematikId;
    }

    public String generateNachricht(){
        Gson gson = new Gson();
        String[] gps = gpsSimulator();
        int distance = distanceSimulator();
        Nachricht nachricht = new Nachricht(telematikId , gps[0], gps[1], distance, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        return gson.toJson(nachricht);
    }

    private String[] gpsSimulator(){
        // Simuliert zufällige GPS Koordinaten zwischen Rosenheim und Hamburg auf Grundlage folgender Werte (gerundet auf 3 Nachkommastellen):
        // Hamburg -> Breitengrad 53.550 ; Längengrad 10.001
        // Rosenheim -> Breitengrad 47.848 ; Längengrad 12.116

        Random random = new Random();
        int vorkommaBreitengrad = 47 + random.nextInt(7);
        int vorkommaLaengengrad = 10 + random.nextInt(3);
        int nachkommaBreitengrad = random.nextInt(848);
        int nachkommaLaengengrad = random.nextInt(848);

        String breitengrad = String.format("%d.%03d", vorkommaBreitengrad, nachkommaBreitengrad);
        String laengengrad = String.format("%d.%03d", vorkommaLaengengrad, nachkommaLaengengrad );

        return new String[]{breitengrad, laengengrad};
    }

    private int distanceSimulator() {
        // Simuliert gefahrene Strecke innerhalb eines Intervalls zwischen 1 und 100 km
        Random random = new Random();
        return (1 + random.nextInt(100));
    }
}
