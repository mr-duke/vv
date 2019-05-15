package de.thro.inf.reactive;

public class Ereignis {

    //Kommentar hinzu
    private String mitarbeiterId;
    private Mitarbeiter.Richtung richtung;

    public Ereignis () {};

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

    public void setMitarbeiterId(String mitarbeiterId) {
        this.mitarbeiterId = mitarbeiterId;
    }

    public void setRichtung(Mitarbeiter.Richtung richtung) {
        this.richtung = richtung;
    }
}
