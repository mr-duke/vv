package de.thro.vv.kleiderkreisel;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Mitglied {

    @Id
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
    @Version
    private Long version;

    @OneToMany(mappedBy = "besitzer")
    List<Kleidung> kleider;

    @JsonIgnore
    @OneToMany(mappedBy = "kaeufer")
    private List<Tausch> kaeufe;

    @JsonIgnore
    @OneToMany(mappedBy = "verkaeufer")
    private List<Tausch> verkaeufe;


    public Mitglied() {
    }

    // Falls Foto nicht angegeben
    public Mitglied(Long nummer, String nachname, String vorname, String email, Adresse adresse, String password, long kontostand) {
        this (nummer, nachname, vorname, email, adresse, null, password, kontostand);
    }

    public Mitglied(Long nummer, String nachname, String vorname, String email, Adresse adresse, String foto, String password, long kontostand) {
        this.nummer = nummer;
        this.nachname = nachname;
        this.vorname = vorname;
        this.email = email;
        this.adresse = adresse;
        this.foto = foto;
        this.password = password;
        this.kontostand = kontostand;
        this.version = 0L;
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

    public void setPostAdresse(Adresse adresse) {
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
