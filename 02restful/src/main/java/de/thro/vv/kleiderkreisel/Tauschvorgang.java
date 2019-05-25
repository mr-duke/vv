package de.thro.vv.kleiderkreisel;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Tauschvorgang {

    @Id
    @GeneratedValue
    private Long id;
    private LocalDate tauschdatum;
    @OneToOne
    private Mitglied verkaeufer;
    @OneToOne
    private Mitglied kaeufer;
    @Version
    private Long version;

    public Tauschvorgang() { }

    public Tauschvorgang(LocalDate tauschdatum, Mitglied verkaeufer, Mitglied kaeufer) {
        this.tauschdatum = tauschdatum;
        this.verkaeufer = verkaeufer;
        this.kaeufer = kaeufer;
        this.version = 0L;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTauschdatum() {
        return tauschdatum;
    }

    public void setTauschdatum(LocalDate tauschdatum) {
        this.tauschdatum = tauschdatum;
    }

    public Mitglied getVerkaeufer() {
        return verkaeufer;
    }

    public void setVerkaeufer(Mitglied verkaeufer) {
        this.verkaeufer = verkaeufer;
    }

    public Mitglied getKaeufer() {
        return kaeufer;
    }

    public void setKaeufer(Mitglied kaeufer) {
        this.kaeufer = kaeufer;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
