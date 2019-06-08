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


    //@Test
    public void tauschplattformTest(){

        KleidungControllerProxy kproxy = new KleidungControllerProxy();
        MitgliedControllerProxy mproxy = new MitgliedControllerProxy();

        Adresse a = new Adresse("Asgardstr.1", "999", "Asgard");
        Mitglied m10 = new Mitglied("Odinson", "Thor", "thor@asgard.ag", a, "hammer", 1000);
        Mitglied m11 = new Mitglied("Allvater", "Odin", "thor@asgard.ag", a, "hammer", 1000);
        Kleidung k5 = new Kleidung(10000L, 5000L, Kleidung.Kleidergroesse.M, Kleidung.Geschlecht.M, Kleidung.Typ.ANZUG, "Bos" );
        Kleidung k6 = new Kleidung(10000L, 5000L, Kleidung.Kleidergroesse.S, Kleidung.Geschlecht.M, Kleidung.Typ.ANZUG, "Boss" );
        Kleidung k7 = new Kleidung(10000L, 5000L, Kleidung.Kleidergroesse.L, Kleidung.Geschlecht.M, Kleidung.Typ.ANZUG, "Bosss" );

        Mitglied thor = mproxy.createNewMitglied(m10);
        Mitglied odin = mproxy.createNewMitglied(m11);
        long odinId = odin.getNummer();
        long thorId= thor.getNummer();
        thor.setKleider(Arrays.asList(k5));
        odin.setKleider(Arrays.asList(k7));

        k6.setBesitzer(odin);

        kproxy.createNewKleidung(k5);
        kproxy.createNewKleidung(k6);
        kproxy.createNewKleidung(k7);

        //Mitglied thor2 = mproxy.findMitgliedById(thorId);
        //mproxy.deleteMitglied(thor2);
    }
}
