package de.thro.vv.kleiderkreisel.server.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Entity
public class Mitglied {

    // Erlaubte Maximalzahl an Kleider pro Mitglied
    @Transient
    private final int MAX_ANZAHL_KLEIDER = 10;

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

    @OneToMany(mappedBy = "besitzer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Kleidung> kleider = new LinkedList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "kaeufer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tausch> kaeufe = new LinkedList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "verkaeufer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tausch> verkaeufe = new LinkedList<>();

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
        this.password = encodePassword(password);
        this.kontostand = kontostand;
        this.version = 0L;
    }

    // Einfache Varainte des Passwort-Hashing mit BCrypt um Passwörter im Klartext zu vermeiden
    private String encodePassword (String password) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    // add und remove-Methoden zur Synchronisierung der bidirektionalen Beziehung zwischen Mitglied und Kleidung
    public void addKleidung(Kleidung kleidung) {
        // Prüfe, ob Anzahl an maximal erlaubten Kleidungsstücken pro Mitglied überschritten
        if (getKleider().size() < MAX_ANZAHL_KLEIDER) {
            kleider.add(kleidung);
            kleidung.setBesitzer(this);
        } else {
            throw new IllegalArgumentException("Maximalzahl erlaubter Kleidungsstück erreicht!");
        }
    }

    public void removeKleidung (Kleidung kleidung) {
        kleider.remove(kleidung);
        kleidung.setBesitzer(null);
    }
    // add und remove-Methoden zur Synchronisierung der bidirektionalen Beziehung zwischen Mitglied und Tausch (Kauf und Verkauf)
    public void addKaeufe(Tausch tausch) {
        kaeufe.add(tausch);
        tausch.setKaeufer(this);
    }

    public void removeKaeufe (Tausch tausch) {
        kaeufe.remove(tausch);
        tausch.setKaeufer(null);
    }

    public void addVerkaeufe(Tausch tausch) {
        verkaeufe.add(tausch);
        tausch.setVerkaeufer(this);
    }

    public void removeVerkaeufe (Tausch tausch) {
        verkaeufe.remove(tausch);
        tausch.setVerkaeufer(null);
    }

    public List<Kleidung> getKleider() {
        return kleider;
    }

    public void setKleider(List<Kleidung> kleider) {
        this.kleider = kleider;
    }

    public List<Tausch> getKaeufe() {
        return kaeufe;
    }

    public void setKaeufe(List<Tausch> kaeufe) {
        this.kaeufe = kaeufe;
    }

    public List<Tausch> getVerkaeufe() {
        return verkaeufe;
    }

    public void setVerkaeufe(List<Tausch> verkaeufe) {
        this.verkaeufe = verkaeufe;
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
        this.password = encodePassword(password);
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
