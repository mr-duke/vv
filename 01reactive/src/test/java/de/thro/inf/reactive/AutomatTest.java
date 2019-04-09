package de.thro.inf.reactive;

import org.junit.Test;

import static org.junit.Assert.*;

public class AutomatTest {

    @Test
    public void schalte() {
        Automat a = new Automat();
        assertEquals(Automat.Zustand.ROT, a.schalte(Automat.Eingabe.TICK));
    }
}