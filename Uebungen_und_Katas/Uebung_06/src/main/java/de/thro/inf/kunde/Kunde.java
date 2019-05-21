package de.thro.inf.kunde;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table (name = "kundentabelle")
public class Kunde {

    @Id
    private long nummer;
    @Column(name = "surname")
    private String name;
    @Column (name = "firstname")
    private String vorname;

    public Kunde(long nummer, String name, String vorname) {
        this.nummer = nummer;
        this.name = name;
        this.vorname = vorname;
    }

    public Kunde(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Kunde)) return false;
        Kunde kunde = (Kunde) o;
        return nummer == kunde.nummer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nummer);
    }

    public long getNummer() {
        return nummer;
    }

    public void setNummer(long nummer) {
        this.nummer = nummer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }
}
