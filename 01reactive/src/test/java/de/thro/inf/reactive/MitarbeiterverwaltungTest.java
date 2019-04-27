package de.thro.inf.reactive;

import org.junit.Test;

import static org.junit.Assert.*;

public class MitarbeiterverwaltungTest {

    @Test
    public void newEmployeeShouldBeAdded(){
        Mitarbeiterverwaltung verwaltung = Mitarbeiterverwaltung.getMitarbeiterverwaltung();
        verwaltung.notify(new Ereignis("01-02-03-aa-bb-cc", Mitarbeiter.Richtung.LINKS ));
        assertEquals(1, verwaltung.getMitarbeiter().size());
        verwaltung.notify(new Ereignis("11-12-13-aa-bb-cc", Mitarbeiter.Richtung.LINKS ));
        verwaltung.notify(new Ereignis("01-02-03-aa-bb-cc", Mitarbeiter.Richtung.LINKS ));
        assertEquals(2, verwaltung.getMitarbeiter().size());
    }
}
