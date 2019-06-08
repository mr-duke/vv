package de.thro.vv.kleiderkreisel.server.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Tausch {

    @Id
    @GeneratedValue
    private Long id;
    // @Convert(converter = DatumsConverter.class)
    private LocalDateTime tauschdatum;
    @JsonIgnore
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "verkaeufer_id")
    private Mitglied verkaeufer;
    @JsonIgnore
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "kaeufer_id")
    private Mitglied kaeufer;
    // Optimistische Strategie über Versionszähler zur Vermeidung des Lost-Update-Problems
    @Version
    private Long version;

    public Tausch() { }

    public Tausch(LocalDateTime tauschdatum) {
        this.tauschdatum = tauschdatum;
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
