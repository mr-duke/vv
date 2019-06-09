package de.thro.vv.kleiderkreisel;

import de.thro.vv.kleiderkreisel.client.KleidungControllerProxy;
import de.thro.vv.kleiderkreisel.client.MitgliedControllerProxy;
import de.thro.vv.kleiderkreisel.client.TauschControllerProxy;
import de.thro.vv.kleiderkreisel.server.entities.Adresse;
import de.thro.vv.kleiderkreisel.server.entities.Kleidung;
import de.thro.vv.kleiderkreisel.server.entities.Mitglied;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;



// Kommentar von Karl Herzog:
// WICHTIG: Dieser Test bereinigt Datenbank nicht, um nach Testablauf konkrete Werte in Datenbank sehen zu können!
// Diesen Test immer UNABHÄNGIG von den beiden anderen Unit-Tests laufen lassen, sonst Verfälschung der Tests durch alte Daten



@RunWith(SpringRunner.class)
@SpringBootTest
public class KleiderkreiselApplicationSimulation {

    @Test
    public void contextLoads() {
    }

    @Test
    public void tauschplattform(){

        KleidungControllerProxy kproxy = new KleidungControllerProxy();
        MitgliedControllerProxy mproxy = new MitgliedControllerProxy();
        TauschControllerProxy tproxy = new TauschControllerProxy();

        // Mitglieder anlegen
        Adresse a = new Adresse("Asgardstr.1", "999", "Asgard");
        Mitglied m1 = new Mitglied("Odinson", "Thor", "thor@asgard.ag", a, "hammer", 20000);
        Mitglied m2 = new Mitglied("Allvater", "Odin", "odin@asgard.ag", a, "power", 40000);
        Mitglied m3 = new Mitglied("Helheim", "Hela", "hela@asgard.de", a, "schwarz", 10000);

        // Kleider anlegen
        Kleidung k1 = new Kleidung(10000L, 5000L, Kleidung.Kleidergroesse.L, Kleidung.Geschlecht.M, Kleidung.Typ.ANZUG, "Boss" );
        Kleidung k2 = new Kleidung(1000L, 400L, Kleidung.Kleidergroesse.L, Kleidung.Geschlecht.M, Kleidung.Typ.PULLOVER, "Northwind" );
        Kleidung k3 = new Kleidung(5000L, 2000L, Kleidung.Kleidergroesse.L, Kleidung.Geschlecht.M, Kleidung.Typ.SHIRT, "McNeil" );
        Kleidung k4 = new Kleidung(8000L, 4000L, Kleidung.Kleidergroesse.M, Kleidung.Geschlecht.M, Kleidung.Typ.HEMD, "SmartGentleman" );
        Kleidung k5 = new Kleidung(7500L, 2000L, Kleidung.Kleidergroesse.M, Kleidung.Geschlecht.M, Kleidung.Typ.HOSE, "Review");
        Kleidung k6 = new Kleidung(5000L, 2000L, Kleidung.Kleidergroesse.S, Kleidung.Geschlecht.W, Kleidung.Typ.BLUSE, "Zara");
        Kleidung k7 = new Kleidung(10000L, 4500L, Kleidung.Kleidergroesse.S, Kleidung.Geschlecht.W, Kleidung.Typ.ANZUG, "Nordic Style");

        // Mitgliedern Kleider zuweisen
        m1.addKleidung(k1);
        m1.addKleidung(k2);
        m1.addKleidung(k3);
        m2.addKleidung(k4);
        m2.addKleidung(k5);
        m3.addKleidung(k6);
        m3.addKleidung(k7);

        // Mitglieder abspeichern
        Mitglied thor = mproxy.createNewMitglied(m1);
        Mitglied odin = mproxy.createNewMitglied(m2);
        Mitglied hela = mproxy.createNewMitglied(m3);
        long thorId = thor.getNummer();
        long odinId = odin.getNummer();
        long helaId = hela.getNummer();

        // Tauschvorgänge anlegen
        tproxy.createNewTausch(mproxy.findMitgliedById(odinId), mproxy.findMitgliedById(thorId));
        tproxy.createNewTausch(mproxy.findMitgliedById(thorId), mproxy.findMitgliedById(odinId));
        tproxy.createNewTausch(mproxy.findMitgliedById(thorId), mproxy.findMitgliedById(odinId));
        tproxy.createNewTausch(mproxy.findMitgliedById(thorId), mproxy.findMitgliedById(helaId));
        tproxy.createNewTausch(mproxy.findMitgliedById(helaId), mproxy.findMitgliedById(thorId));

        // Datenbank bereinigen falls nötig
        // mproxy.deleteMitglied(mproxy.findMitgliedById(odinId));
        // mproxy.deleteMitglied(mproxy.findMitgliedById(helaId));
        // mproxy.deleteMitglied(mproxy.findMitgliedById(thorId));
    }
}
