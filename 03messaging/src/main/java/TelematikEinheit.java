public class TelematikEinheit {

    private long id;
    // Statischer Zähler zum Erzeugen eindeutiger IDs beim Erzeugen eines neuen Objekts im Konstruktor
    public static long idCounter = 1;

    TelematikEinheit() {
        this.id = idCounter;
        idCounter++ ;
    }
}
