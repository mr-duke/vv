package de.thro.inf.reactive;

import org.junit.Test;
import static org.junit.Assert.*;

public class MitarbeiterTest {


    @Test
    public void zustandShouldBeImGang(){
        Mitarbeiter m = new Mitarbeiter();
        m.bewegen(m.getAktuellerZustand(), Mitarbeiter.Richtung.LINKS);
        assertEquals(Mitarbeiter.Zustand.IMGANG, m.getAktuellerZustand());
        m.bewegen(m.getAktuellerZustand(), Mitarbeiter.Richtung.RECHTS);
        m.bewegen(m.getAktuellerZustand(), Mitarbeiter.Richtung.RECHTS);
        assertEquals(Mitarbeiter.Zustand.IMGANG, m.getAktuellerZustand());
    }

    @Test
    public void zustandShouldBeAbwesend(){
        Mitarbeiter m = new Mitarbeiter();
        assertEquals(Mitarbeiter.Zustand.ABWESEND, m.getAktuellerZustand());
        m.bewegen(m.getAktuellerZustand(), Mitarbeiter.Richtung.LINKS);
        m.bewegen(m.getAktuellerZustand(), Mitarbeiter.Richtung.LINKS);
        assertEquals(Mitarbeiter.Zustand.ABWESEND, m.getAktuellerZustand());
    }

    @Test
    public void zustandShouldBeAnwesend(){
        Mitarbeiter m = new Mitarbeiter();
        m.bewegen(m.getAktuellerZustand(), Mitarbeiter.Richtung.LINKS);
        m.bewegen(m.getAktuellerZustand(), Mitarbeiter.Richtung.RECHTS);
        assertEquals(Mitarbeiter.Zustand.ANWESEND, m.getAktuellerZustand());
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfRechtsIsFirst(){
        Mitarbeiter m = new Mitarbeiter();
        m.bewegen(m.getAktuellerZustand(), Mitarbeiter.Richtung.RECHTS);
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfIllegalMovement(){
        Mitarbeiter m = new Mitarbeiter();
        m.bewegen(m.getAktuellerZustand(), Mitarbeiter.Richtung.LINKS);
        m.bewegen(m.getAktuellerZustand(), Mitarbeiter.Richtung.RECHTS);
        m.bewegen(m.getAktuellerZustand(), Mitarbeiter.Richtung.LINKS);
    }
}
