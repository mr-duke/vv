package de.thro.inf.reactive;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.Assert.*;

public class MitarbeiterverwaltungTest {

    @Test
    public void newEmployeeShouldBeAdded() throws IOException {
        Mitarbeiterverwaltung verwaltung = Mitarbeiterverwaltung.getMitarbeiterverwaltung();
        verwaltung.notify(new Ereignis("01-02-03-aa-bb-cc", Mitarbeiter.Richtung.LINKS ));
        assertEquals(1, verwaltung.getMitarbeiter().size());
        verwaltung.notify(new Ereignis("11-12-13-aa-bb-cc", Mitarbeiter.Richtung.LINKS ));
        verwaltung.notify(new Ereignis("01-02-03-aa-bb-cc", Mitarbeiter.Richtung.RECHTS ));
        assertEquals(2, verwaltung.getMitarbeiter().size());
    }
}
