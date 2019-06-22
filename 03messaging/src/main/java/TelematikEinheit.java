import java.util.Random;

public class TelematikEinheit {

    // Sendeintervall der Nachrichten in Millisekunden
    private final long TIME_INTERVALL_SEND = 5000 ;

    private long id;
    // Statischer Zähler zum Erzeugen eindeutiger IDs beim Erzeugen eines neuen Objekts im Konstruktor
    public static long idCounter = 1;


    TelematikEinheit() {
        this.id = idCounter;
        idCounter++ ;
    }

    public String[] gpsSimulator (){
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
}
