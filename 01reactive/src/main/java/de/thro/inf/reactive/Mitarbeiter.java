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

    private Zustand uebergang [][] = {
            {Zustand.IMGANG, Zustand.ABWESEND,Zustand.ERROR, null },
            {Zustand.ERROR, Zustand.ANWESEND, Zustand.IMGANG, null }
    };

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
