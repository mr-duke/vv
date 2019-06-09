package de.thro.vv.kleiderkreisel;

import de.thro.vv.kleiderkreisel.client.KleidungControllerProxy;
import de.thro.vv.kleiderkreisel.client.MitgliedControllerProxy;
import de.thro.vv.kleiderkreisel.client.TauschControllerProxy;
import de.thro.vv.kleiderkreisel.server.entities.Adresse;
import de.thro.vv.kleiderkreisel.server.entities.Kleidung;
import de.thro.vv.kleiderkreisel.server.entities.Mitglied;
import de.thro.vv.kleiderkreisel.server.entities.Tausch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KleiderkreiselApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void tauschplattformTest(){

        KleidungControllerProxy kproxy = new KleidungControllerProxy();
        MitgliedControllerProxy mproxy = new MitgliedControllerProxy();
        TauschControllerProxy tproxy = new TauschControllerProxy();

        Adresse a = new Adresse("Asgardstr.1", "999", "Asgard");
        Mitglied m1 = new Mitglied("Odinson", "Thor", "thor@asgard.ag", a, "hammer", 10000);
        Mitglied m2 = new Mitglied("Allvater", "Odin", "odin@asgard.ag", a, "power", 20000);

        Kleidung k1 = new Kleidung(10000L, 5000L, Kleidung.Kleidergroesse.M, Kleidung.Geschlecht.M, Kleidung.Typ.ANZUG, "Boss" );
        Kleidung k2 = new Kleidung(1000L, 400L, Kleidung.Kleidergroesse.S, Kleidung.Geschlecht.M, Kleidung.Typ.PULLOVER, "Northwind" );
        Kleidung k3 = new Kleidung(5000L, 2000L, Kleidung.Kleidergroesse.L, Kleidung.Geschlecht.M, Kleidung.Typ.SHIRT, "McNeil" );
        Kleidung k4 = new Kleidung(8000L, 4000L, Kleidung.Kleidergroesse.L, Kleidung.Geschlecht.M, Kleidung.Typ.HEMD, "SmartGentleman" );

        Tausch t1 = new Tausch(LocalDateTime.now());
        Tausch t2 = new Tausch(LocalDateTime.now().minusMinutes(10));

        m1.addKleidung(k1);
        m1.addKleidung(k2);
        m1.addKleidung(k3);
        m2.addKleidung(k4);

        Mitglied thor = mproxy.createNewMitglied(m1);
        Mitglied odin = mproxy.createNewMitglied(m2);
        long thorId = thor.getNummer();
        long odinId = odin.getNummer();


        tproxy.createNewTausch(mproxy.findMitgliedById(odinId), mproxy.findMitgliedById(thorId));
        //t1.setVerkaeufer(thor);
        //t1.setKaeufer(odin);
        //tproxy.createNewTausch(thor, odin);


        tproxy.createNewTausch(mproxy.findMitgliedById(thorId), mproxy.findMitgliedById(odinId));


        tproxy.createNewTausch(mproxy.findMitgliedById(thorId), mproxy.findMitgliedById(odinId));
        //Mitglied thor = mproxy.findMitgliedById(1L);
        //mproxy.deleteMitglied(thor);
    }
}
