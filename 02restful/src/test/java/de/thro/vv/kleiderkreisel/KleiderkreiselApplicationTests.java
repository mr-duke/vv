package de.thro.vv.kleiderkreisel;

import de.thro.vv.kleiderkreisel.client.KleidungControllerProxy;
import de.thro.vv.kleiderkreisel.client.MitgliedControllerProxy;
import de.thro.vv.kleiderkreisel.server.entities.Adresse;
import de.thro.vv.kleiderkreisel.server.entities.Kleidung;
import de.thro.vv.kleiderkreisel.server.entities.Mitglied;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;

import java.lang.reflect.Array;
import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KleiderkreiselApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test (expected = RestClientException.class)
    public void mitgliedCrudTest(){
        MitgliedControllerProxy mproxy = new MitgliedControllerProxy();

        // createNew Mitglied-Test
        Adresse a = new Adresse("Asgardstr.1", "999", "Asgard");
        Mitglied m1 = new Mitglied("Odinson", "Thor", "thor@asgard.ag", a, "hammer", 1000);
        Mitglied m2 = new Mitglied("Allvater", "Odin", "odin@asgard.ag", a, "power", 10000);

        Mitglied thor = mproxy.createNewMitglied(m1);
        Mitglied odin = mproxy.createNewMitglied(m2);
        long thorId = thor.getNummer();
        long odinId = odin.getNummer();

        assertNotNull(mproxy.findMitgliedById(thorId));
        assertNotNull(mproxy.findMitgliedById(odinId));

        // findMitgliedByID-Test
        assertEquals("Thor", mproxy.findMitgliedById(thorId).getVorname());

        // findAllMitglieder-Test
        // Liste aller Mitglieder besteht aus 2 Mitgliedern
        assertEquals(2, mproxy.findAllMitglieder().size());

        // updateMitglied-Test
        assertEquals(1000, thor.getKontostand());
        // Kontostand updaten
        thor.setKontostand(2000);
        Mitglied thorNew = mproxy.updateMitglied(thor);
        assertEquals(2000, thorNew.getKontostand());

        //deleteMitglied-Test
        Mitglied thor2 = mproxy.findMitgliedById(thorId);
        Mitglied odin2 = mproxy.findMitgliedById(odinId);
        mproxy.deleteMitglied(thor2);
        mproxy.deleteMitglied(odin2);
        // Wirft RestClientException; getestet in Methodensignatur (expected = RestClientException.class)
        mproxy.findAllMitglieder();
    }

    @Test //(expected = IllegalArgumentException.class)
    public void kleidungCrudTest(){
        KleidungControllerProxy kproxy = new KleidungControllerProxy();
        MitgliedControllerProxy mproxy = new MitgliedControllerProxy();

        Adresse a = new Adresse("Asgardstr.1", "999", "Asgard");
        Mitglied m1 = new Mitglied("Odinson", "Thor", "thor@asgard.ag", a, "hammer", 1000);
        Mitglied m2 = new Mitglied("Allvater", "Odin", "odin@asgard.ag", a, "power", 10000);


        // createNewKleidung-Test
        Kleidung k1 = new Kleidung(10000L, 5000L, Kleidung.Kleidergroesse.M, Kleidung.Geschlecht.M, Kleidung.Typ.ANZUG, "Boss" );
        Kleidung k2 = new Kleidung(5000L, 2000L, Kleidung.Kleidergroesse.S, Kleidung.Geschlecht.W, Kleidung.Typ.HEMD, "Mango");
        // Preis zu niedrig - > wirft IllegalArgumentException getestet in Methodensignatur (expected = IllegalArgumentException.class)
        //Kleidung k3 = new Kleidung(1000L, 10L, Kleidung.Kleidergroesse.L, Kleidung.Geschlecht.M, Kleidung.Typ.SHIRT, "C+A" );
        // Preis zu hoch - > wirft IllegalArgumentException getestet in Methodensignatur (expected = IllegalArgumentException.class)
        //Kleidung k4 = new Kleidung(1000L, 800L, Kleidung.Kleidergroesse.L, Kleidung.Geschlecht.M, Kleidung.Typ.SHIRT, "Kick" );

        mproxy.createNewMitglied(m1);
        //m1.setKleider(Arrays.asList(k1, k2));

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
        //Kleidung anzug2 = kproxy.findKleidungById(anzugId);
        //Kleidung hemd2 = kproxy.findKleidungById(hemdId);
        //kproxy.deleteKleidung(anzug2);
        //kproxy.deleteKleidung(hemd2);
    }

    @Test (expected = RestClientException.class)
    public void kleidungDeleteTest(){
        KleidungControllerProxy kproxy = new KleidungControllerProxy();
        MitgliedControllerProxy mproxy = new MitgliedControllerProxy();

        Adresse a = new Adresse("Asgardstr.1", "999", "Asgard");
        Mitglied m1 = new Mitglied("Odinson", "Thor", "thor@asgard.ag", a, "hammer", 1000);
        Mitglied m2 = new Mitglied("Allvater", "Odin", "odin@asgard.ag", a, "power", 10000);

        Mitglied thor = mproxy.createNewMitglied(m1);

        // createNewKleidung-Test
        Kleidung k1 = new Kleidung(10000L, 5000L, Kleidung.Kleidergroesse.M, Kleidung.Geschlecht.M, Kleidung.Typ.ANZUG, "Boss" );
        Kleidung k2 = new Kleidung(5000L, 2000L, Kleidung.Kleidergroesse.S, Kleidung.Geschlecht.W, Kleidung.Typ.HEMD, "Mango");
        Kleidung anzug = kproxy.createNewKleidung(k1);
        Kleidung hemd = kproxy.createNewKleidung(k2);

        kproxy.deleteKleidung(anzug);
        kproxy.deleteKleidung(hemd);
        // Wirft RestClientException; getestet in Methodensignatur (expected = RestClientException.class)
        kproxy.findAllKleider();
    }

    @Test
    public void kleidungTest(){
        KleidungControllerProxy kproxy = new KleidungControllerProxy();
        MitgliedControllerProxy mproxy = new MitgliedControllerProxy();

        Adresse a = new Adresse("Asgardstr.1", "999", "Asgard");
        Mitglied m10 = new Mitglied("Odinson", "Thor", "thor@asgard.ag", a, "hammer", 1000);
        Kleidung k5 = new Kleidung(10000L, 5000L, Kleidung.Kleidergroesse.M, Kleidung.Geschlecht.M, Kleidung.Typ.ANZUG, "Boss" );

        mproxy.createNewMitglied(m10);
        kproxy.createNewKleidung(k5);
        m10.setKleider(Arrays.asList(k5));



    }
}
