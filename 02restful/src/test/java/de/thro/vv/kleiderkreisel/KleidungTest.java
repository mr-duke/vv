package de.thro.vv.kleiderkreisel;

import de.thro.vv.kleiderkreisel.client.KleidungControllerProxy;
import de.thro.vv.kleiderkreisel.server.entities.Kleidung;
import org.junit.Test;
import org.springframework.web.client.RestClientException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class KleidungTest {

    @Test
    public void kleidungCrudTest(){
        KleidungControllerProxy kproxy = new KleidungControllerProxy();

        // createNewKleidung-Test
        Kleidung k1 = new Kleidung(10000L, 5000L, Kleidung.Kleidergroesse.M, Kleidung.Geschlecht.M, Kleidung.Typ.ANZUG, "Boss" );
        Kleidung k2 = new Kleidung(5000L, 2000L, Kleidung.Kleidergroesse.S, Kleidung.Geschlecht.W, Kleidung.Typ.HEMD, "Mango");

        Kleidung anzug = kproxy.createNewKleidung(k1);
        Kleidung hemd = kproxy.createNewKleidung(k2);
        long anzugId = anzug.getId();
        long hemdId = hemd.getId();

        assertNotNull(kproxy.findKleidungById(anzugId));
        assertNotNull(kproxy.findKleidungById(hemdId));

        // findKleidungByID-Test
        assertEquals("Boss", kproxy.findKleidungById(anzugId).getHersteller());

        // findAllKleider-Test
        // Liste aller Kleider besteht aus 2 Kleidungsstücken
        assertEquals(2, kproxy.findAllKleider().size());

        // updateKleidung-Test
        assertEquals(10000, anzug.getNeupreis());
        // Kontostand updaten
        anzug.setNeupreis(15000);
        Kleidung anzugNew = kproxy.updateKleidung(anzug);
        assertEquals(15000, anzugNew.getNeupreis());

        //Datenbank wieder aufräumen. Eigener Test für deleteKleidung siehe unten
        Kleidung anzug2 = kproxy.findKleidungById(anzugId);
        Kleidung hemd2 = kproxy.findKleidungById(hemdId);
        kproxy.deleteKleidung(anzug2);
        kproxy.deleteKleidung(hemd2);
    }

    @Test (expected = RestClientException.class)
    public void kleidungDeleteTest(){
        KleidungControllerProxy kproxy = new KleidungControllerProxy();

        Kleidung k1 = new Kleidung(10000L, 5000L, Kleidung.Kleidergroesse.M, Kleidung.Geschlecht.M, Kleidung.Typ.ANZUG, "Boss" );
        Kleidung k2 = new Kleidung(5000L, 2000L, Kleidung.Kleidergroesse.S, Kleidung.Geschlecht.W, Kleidung.Typ.HEMD, "Mango");
        Kleidung anzug = kproxy.createNewKleidung(k1);
        Kleidung hemd = kproxy.createNewKleidung(k2);

        kproxy.deleteKleidung(anzug);
        kproxy.deleteKleidung(hemd);
        // Wirft RestClientException; getestet in Methodensignatur (expected = RestClientException.class)
        kproxy.findAllKleider();
    }

    @Test (expected = IllegalArgumentException.class)
    public void illegalPriceTest(){
        KleidungControllerProxy kproxy = new KleidungControllerProxy();
        // Preis zu niedrig - > wirft IllegalArgumentException getestet in Methodensignatur (expected = IllegalArgumentException.class)
        Kleidung k3 = new Kleidung(1000L, 10L, Kleidung.Kleidergroesse.L, Kleidung.Geschlecht.M, Kleidung.Typ.SHIRT, "C+A" );
        // Preis zu hoch - > wirft IllegalArgumentException getestet in Methodensignatur (expected = IllegalArgumentException.class)
        Kleidung k4 = new Kleidung(1000L, 800L, Kleidung.Kleidergroesse.L, Kleidung.Geschlecht.M, Kleidung.Typ.SHIRT, "Kick" );
    }
}
