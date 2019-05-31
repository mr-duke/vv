package de.thro.vv.kleiderkreisel;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Tausch {

    @Id
    @GeneratedValue
    private Long id;
    // @Convert(converter = DatumsConverter.class)
    private LocalDate tauschdatum;
    @ManyToOne
    private Mitglied verkaeufer;
    @ManyToOne
    private Mitglied kaeufer;
    @Version
    private Long version;

    public Tausch() { }

    public Tausch(LocalDate tauschdatum) {
        this.tauschdatum = tauschdatum;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tausch tausch = (Tausch) o;
        return id.equals(tausch.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
