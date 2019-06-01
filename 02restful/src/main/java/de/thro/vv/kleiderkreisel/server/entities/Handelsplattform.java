package de.thro.vv.kleiderkreisel.server.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Handelsplattform {

    // ID des Kontos
    // Handelplattform könnte mehrere Konten besitzen; in diesem Beispiel wird der Einfachheit halber
    // nur über ein und dasselbe Konto abgerechnet
    @Id
    private Long id;
    // Kontostand der Handelbörse; Verkäufer und Käufer zahlen  je 50 Cent pro Tausch
    private long kontostand;
    // Datum der letzten Kontobewegung
    private LocalDateTime zuletztGeaendert;

    public Handelsplattform() {
    }

    public Handelsplattform(Long id, long kontostand, LocalDateTime zuletztGeaendert) {
        this.id = id;
        this.kontostand = kontostand;
        this.zuletztGeaendert = zuletztGeaendert;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getKontostand() {
        return kontostand;
    }

    public void setKontostand(long kontostand) {
        this.kontostand = kontostand;
    }

    public LocalDateTime getZuletztGeaendert() {
        return zuletztGeaendert;
    }

    public void setZuletztGeaendert(LocalDateTime zuletztGeaendert) {
        this.zuletztGeaendert = zuletztGeaendert;
    }
}
