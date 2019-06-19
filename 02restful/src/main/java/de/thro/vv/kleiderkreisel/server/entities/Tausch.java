package de.thro.vv.kleiderkreisel.server.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Tausch {

    @Id
    @GeneratedValue
    private Long id;
    private LocalDateTime tauschdatum;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn (name = "verkaeufer_id")
    private Mitglied verkaeufer;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn (name = "kaeufer_id")
    private Mitglied kaeufer;

    private String marke;

    // Optimistische Strategie über Versionszähler zur Vermeidung des Lost-Update-Problems
    @Version
    private Long version;
    public Tausch() { }

    public Tausch(LocalDateTime tauschdatum, String marke) {
        this.tauschdatum = tauschdatum;
        this.marke = marke;
        this.version = 0L;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTauschdatum() {
        return tauschdatum;
    }

    public void setTauschdatum(LocalDateTime tauschdatum) {
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

    public String getMarke() {
        return marke;
    }

    public void setMarke(String marke) {
        this.marke = marke;
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
