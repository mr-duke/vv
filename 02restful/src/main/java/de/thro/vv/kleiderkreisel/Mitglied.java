package de.thro.vv.kleiderkreisel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import java.util.Objects;

@Entity
public class Mitglied {

    @Id
    private String nickname;
    private String nachname;
    private String vorname;
    private String email;
    private String postAdresse;
    private String foto;
    private String password;
    // Kontostand in Euro
    private double kontostand;
    @Version
    private Long version;

    public Mitglied() {
    }

    // Falls Foto nicht angegeben
    public Mitglied(String nickname, String nachname, String vorname, String email, String postAdresse, String password, double kontostand) {
        this (nickname, nachname, vorname, email, postAdresse, null, password, kontostand);
    }

    public Mitglied(String nickname, String nachname, String vorname, String email, String postAdresse, String foto, String password, double kontostand) {
        this.nickname = nickname;
        this.nachname = nachname;
        this.vorname = vorname;
        this.email = email;
        this.postAdresse = postAdresse;
        this.foto = foto;
        this.password = password;
        this.kontostand = kontostand;
        this.version = 0L;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public String getPostAdresse() {
        return postAdresse;
    }

    public void setPostAdresse(String postAdresse) {
        this.postAdresse = postAdresse;
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

    public double getKontostand() {
        return kontostand;
    }

    public void setKontostand(double kontostand) {
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
        return nickname.equals(mitglied.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname);
    }

    @Override
    public String toString() {
        return "Mitglied{" +
                "nickname='" + nickname + '\'' +
                ", nachname='" + nachname + '\'' +
                ", vorname='" + vorname + '\'' +
                '}';
    }
}
