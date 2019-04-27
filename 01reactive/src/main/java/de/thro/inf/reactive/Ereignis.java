package de.thro.inf.reactive;

public class Ereignis {

    private String mitarbeiterId;
    private Mitarbeiter.Richtung richtung;

    public Ereignis(String mitarbeiterId, Mitarbeiter.Richtung richtung){
        this.mitarbeiterId = mitarbeiterId;
        this.richtung = richtung;
    }

    public String getMitarbeiterId() {
        return mitarbeiterId;
    }

    public Mitarbeiter.Richtung getRichtung() {
        return richtung;
    }

}
