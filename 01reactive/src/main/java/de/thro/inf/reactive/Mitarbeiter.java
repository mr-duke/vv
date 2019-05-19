package de.thro.inf.reactive;

public class Mitarbeiter {
    private String id;
    private Zustand aktuellerZustand;

    public Mitarbeiter(String id){
        this.id = id;
        this.aktuellerZustand = Zustand.ABWESEND;
    }

    public enum Zustand {ABWESEND, IMGANG, ANWESEND, ERROR};
    public enum Richtung {LINKS, RECHTS};

    // Zustandüberstandsdiagramm laut Angabe
    private Zustand uebergang [][] = {
            {Zustand.IMGANG, Zustand.ABWESEND,Zustand.ERROR, null },
            {Zustand.ERROR, Zustand.ANWESEND, Zustand.IMGANG, null }
    };

    // Mitarbeiter wird neuer Zustand zugewiesen, je nachdem an welchem Sensor er vorbeiläuft
    // Falls Mitarbeiter in Zustand.ERROR gelangen sollte, wird Fehlermeldung geworfen
    // und sein aktuellerZustand wird nicht neu gesetzt bzw. bleibt auf dem vorherigen Zustand!
    // Fehlermeldung wird geloggt
    public void bewegen(Zustand zustand, Richtung richtung){
        Zustand neuerZustand = uebergang [richtung.ordinal()][zustand.ordinal()];
        if (neuerZustand == Zustand.ERROR)
            throw new IllegalArgumentException("Richtung nicht möglich!");
        aktuellerZustand = neuerZustand;
    }

    public Zustand getAktuellerZustand(){
        return aktuellerZustand;
    }

    public String getId(){ return id; }
}
