package de.thro.vv.kleiderkreisel;

import javax.persistence.Embeddable;

@Embeddable
public class Adresse {
    private String strasse;
    private String plz;
    private String stadt;

    public Adresse() {}

    public Adresse(String strasse, String plz, String stadt) {
        this.strasse = strasse;
        this.plz = plz;
        this.stadt = stadt;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public String getStadt() {
        return stadt;
    }

    public void setStadt(String stadt) {
        this.stadt = stadt;
    }

    @Override
    public String toString() {
        return "Adresse{" +
                "strasse='" + strasse + '\'' +
                ", plz='" + plz + '\'' +
                ", stadt='" + stadt + '\'' +
                '}';
    }
}
