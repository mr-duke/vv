package de.thro.inf.reactive;

import configuration.FromConsole;
import org.junit.Test;

public class FromConsoleTest {
    FromConsole input = new FromConsole();

    @Test
    public void print (){
        System.out.println(input.getSensorArt());
        System.out.println(input.getInetAdress());
        System.out.println(input.getPortNumber());
    }
}
