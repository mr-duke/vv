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
        // Nach Tausch bei Odin
        Kleidung k1 = new Kleidung(10000L, 5000L, Kleidung.Kleidergroesse.L, Kleidung.Geschlecht.M, Kleidung.Typ.ANZUG, "Boss" );
        // Bleibt bei Thor
        Kleidung k2 = new Kleidung(1000L, 400L, Kleidung.Kleidergroesse.L, Kleidung.Geschlecht.M, Kleidung.Typ.PULLOVER, "Northwind" );
        // Nach Tausch bei Hela
        Kleidung k3 = new Kleidung(5000L, 2000L, Kleidung.Kleidergroesse.L, Kleidung.Geschlecht.M, Kleidung.Typ.SHIRT, "McNeil" );
        // Nach Tausch bei Thor
        Kleidung k4 = new Kleidung(8000L, 4000L, Kleidung.Kleidergroesse.M, Kleidung.Geschlecht.M, Kleidung.Typ.HEMD, "SmartGentleman" );
        // Nach Tausch bei Thor
        Kleidung k5 = new Kleidung(7500L, 2000L, Kleidung.Kleidergroesse.M, Kleidung.Geschlecht.M, Kleidung.Typ.HOSE, "Review");
        // Bleibt bei Hela
        Kleidung k6 = new Kleidung(5000L, 2000L, Kleidung.Kleidergroesse.S, Kleidung.Geschlecht.W, Kleidung.Typ.BLUSE, "Zara");
        // Nach Tausch bei Thor
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
        Mitglied thorNew = mproxy.createNewMitglied(m1);
        Mitglied odinNew = mproxy.createNewMitglied(m2);
        Mitglied helaNew = mproxy.createNewMitglied(m3);
        long thorId = thorNew.getNummer();
        long odinId = odinNew.getNummer();
        long helaId = helaNew.getNummer();

        // Tauschvorgänge anlegen
        Mitglied odin = mproxy.findMitgliedById(odinId);
        Mitglied thor = mproxy.findMitgliedById(thorId);
        Mitglied hela;

        tproxy.createNewTausch(odin, thor, thor.getKleider().get(0));

        odin = mproxy.findMitgliedById(odinId);
        thor = mproxy.findMitgliedById(thorId);
        tproxy.createNewTausch(thor, odin, odin.getKleider().get(0));

        odin = mproxy.findMitgliedById(odinId);
        thor = mproxy.findMitgliedById(thorId);
        tproxy.createNewTausch(thor, odin, odin.getKleider().get(0));

        thor = mproxy.findMitgliedById(thorId);
        hela = mproxy.findMitgliedById(helaId);
        tproxy.createNewTausch(thor, hela, hela.getKleider().get(1));

        thor = mproxy.findMitgliedById(thorId);
        hela = mproxy.findMitgliedById(helaId);
        tproxy.createNewTausch(hela, thor, thor.getKleider().get(1));


        // Nur um Datenbank zu bereinigen, falls nötig
        // mproxy.deleteMitglied(mproxy.findMitgliedById(odinId));
        // mproxy.deleteMitglied(mproxy.findMitgliedById(helaId));
        // mproxy.deleteMitglied(mproxy.findMitgliedById(thorId));*/
    }
}
