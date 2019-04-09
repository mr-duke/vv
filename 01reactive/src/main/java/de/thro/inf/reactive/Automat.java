package de.thro.inf.reactive;

public class Automat {
    enum Zustand {ROT, GRUEN};
    enum Eingabe {TICK, KNOPF};

    public Zustand schalte (Eingabe eingabe){
        return Zustand.ROT;
    }
}
