package de.thro.vv.kleiderkreisel.server.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Entity
public class Mitglied {

    @Id
    @GeneratedValue
    private Long nummer;
    private String nachname;
    private String vorname;
    private String email;
    @Embedded
    private Adresse adresse;
    private String foto;
    // kommt noch
    private String password;
    // Kontostand in EuroCent
    private long kontostand;
    // Optimistische Strategie über Versionszähler zur Vermeidung des Lost-Update-Problems
    @Version
    private Long version;

    @OneToMany(mappedBy = "besitzer")
    List<Kleidung> kleider;

    // @JsonIgnore
    @OneToMany(mappedBy = "kaeufer")
    private List<Tausch> kaeufe;

    // @JsonIgnore
    @OneToMany(mappedBy = "verkaeufer")
    private List<Tausch> verkaeufe;


    public Mitglied() {
    }

    // Falls Foto nicht angegeben
    public Mitglied( String nachname, String vorname, String email, Adresse adresse, String password, long kontostand) {
        this (nachname, vorname, email, adresse, null, password, kontostand);
    }

    public Mitglied(String nachname, String vorname, String email, Adresse adresse, String foto, String password, long kontostand) {
        this.nachname = nachname;
        this.vorname = vorname;
        this.email = email;
        this.adresse = adresse;
        this.foto = foto;
        this.password = password;
        this.kontostand = kontostand;
        this.version = 0L;
        this.kleider = new LinkedList<>();
        this.kaeufe = new LinkedList<>();
        this.verkaeufe = new LinkedList<>();
    }

    public Long getNummer() {
        return nummer;
    }

    public void setNummer(Long nummer) {
        this.nummer = nummer;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getKontostand() {
        return kontostand;
    }

    public void setKontostand(long kontostand) {
        this.kontostand = kontostand;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<Kleidung> getKleider() {
        return kleider;
    }

    public void setKleider(List<Kleidung> kleider) {
        this.kleider = kleider;
        for (Kleidung kl : kleider){
            kl.setBesitzer(this);
        }
    }

    public List<Tausch> getKaeufe() {
        return kaeufe;
    }

    public void setKaeufe(List<Tausch> kaeufe) {
        this.kaeufe = kaeufe;
        for (Tausch t : kaeufe){
            t.setKaeufer(this);
        }
    }

    public List<Tausch> getVerkaeufe() {
        return verkaeufe;
    }

    public void setVerkaeufe(List<Tausch> verkaeufe) {
        this.verkaeufe = verkaeufe;
        for (Tausch t : verkaeufe){
            t.setVerkaeufer(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mitglied mitglied = (Mitglied) o;
        return nummer.equals(mitglied.nummer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nummer);
    }

    @Override
    public String toString() {
        return "Mitglied{" +
                "nummer='" + nummer + '\'' +
                ", nachname='" + nachname + '\'' +
                ", vorname='" + vorname + '\'' +
                '}';
    }
}
