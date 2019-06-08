package de.thro.vv.kleiderkreisel.server.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Kleidung {

    // Minimaler Anteil vom Neupreis
    @Transient
    private final double PREISGRENZE_MIN = 0.1;
    // Maximaler Anteil vom Neupreis
    @Transient
    private final double PREISGRENZE_MAX = 0.5;

    @Id
    @GeneratedValue
    private Long id;
    // Neupreis in EuroCent
    private long neupreis;
    // Tauschwert in EuroCent
    private long tauschwert;
    private Kleidergroesse groesse;
    private Geschlecht geschlecht;
    private Typ typ;
    private String hersteller;
    // optional
    private String foto;
    // Optimistische Strategie über Versionszähler zur Vermeidung des Lost-Update-Problems
    @Version
    private Long version;

    //@JsonIgnore
    @ManyToOne
    private Mitglied besitzer;

    public enum Kleidergroesse {S, M, L, XL};
    // m = männlich, w = weiblich
    public enum Geschlecht {M, W};
    public enum Typ {HOSE, KLEID, HEMD, BLUSE, SHIRT, PULLOVER, ANZUG}

    public Kleidung() {
    }

    // Falls Foto nicht angegeben
    public Kleidung(long neupreis, long tauschwert, Kleidergroesse groesse, Geschlecht geschlecht, Typ typ, String hersteller) {
        this (neupreis, tauschwert, groesse, geschlecht, typ, hersteller, null);
    }

    public Kleidung(long neupreis, long tauschwert, Kleidergroesse groesse, Geschlecht geschlecht, Typ typ, String hersteller, String foto) {
        this.neupreis = neupreis;
        // Tauschwert muss zwischen 10 und 50 % vom Neupreis liegen
        if (tauschwert >= neupreis*PREISGRENZE_MIN && tauschwert <= neupreis*PREISGRENZE_MAX ) {
            this.tauschwert = tauschwert;
        } else
            throw new IllegalArgumentException("Tauschwert ausserhalb erlaubter Preisspanne");
        this.groesse = groesse;
        this.geschlecht = geschlecht;
        this.typ = typ;
        this.hersteller = hersteller;
        this.foto = foto;
        this.version = 0L;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getNeupreis() {
        return neupreis;
    }

    public void setNeupreis(long neupreis) {
        this.neupreis = neupreis;
    }

    public long getTauschwert() {
        return tauschwert;
    }

    public void setTauschwert(long tauschwert) {
        if (tauschwert >= neupreis*PREISGRENZE_MIN && tauschwert <= neupreis*PREISGRENZE_MAX ) {
            this.tauschwert = tauschwert;
        } else
            throw new IllegalArgumentException("Tauschwert ausserhalb erlaubter Preisspanne");
    }

    public Kleidergroesse getGroesse() {
        return groesse;
    }

    public void setGroesse(Kleidergroesse groesse) {
        this.groesse = groesse;
    }

    public Geschlecht getGeschlecht() {
        return geschlecht;
    }

    public void setGeschlecht(Geschlecht geschlecht) {
        this.geschlecht = geschlecht;
    }

    public Typ getTyp() {
        return typ;
    }

    public void setTyp(Typ typ) {
        this.typ = typ;
    }

    public String getHersteller() {
        return hersteller;
    }

    public void setHersteller(String hersteller) {
        this.hersteller = hersteller;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Mitglied getBesitzer() {
        return besitzer;
    }

    public void setBesitzer(Mitglied besitzer) {
        this.besitzer = besitzer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kleidung kleidung = (Kleidung) o;
        return id.equals(kleidung.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Kleidung{" +
                "id=" + id +
                ", neupreis=" + neupreis +
                ", tauschwert=" + tauschwert +
                ", groesse=" + groesse +
                ", geschlecht=" + geschlecht +
                ", besitzer=" + besitzer +
                '}';
    }
}
