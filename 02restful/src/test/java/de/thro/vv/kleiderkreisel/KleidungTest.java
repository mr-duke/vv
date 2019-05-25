package de.thro.vv.kleiderkreisel;

import org.junit.Test;

public class KleidungTest {

    @Test
    public void kleidungtest() {
        Kleidung kleidung = new Kleidung(100, 15, Kleidung.Kleidergroesse.M, Kleidung.Geschlecht.m, Kleidung.Typ.Hemd, "PC");
        Kleidung kleidung2 = new Kleidung(100, 45, Kleidung.Kleidergroesse.M, Kleidung.Geschlecht.m, Kleidung.Typ.Hemd, "PC");
    }
}
