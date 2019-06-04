package de.thro.vv.kleiderkreisel;

import de.thro.vv.kleiderkreisel.client.MitgliedControllerProxy;
import de.thro.vv.kleiderkreisel.server.entities.Adresse;
import de.thro.vv.kleiderkreisel.server.entities.Mitglied;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KleiderkreiselApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void tauschvorgangTest(){
        MitgliedControllerProxy mproxy = new MitgliedControllerProxy();

        // Create some sample users
        Adresse a = new Adresse("Asgardstr.1", "999", "Asgard");
        Mitglied m1 = new Mitglied("Odinson", "Thor", "thor@asgard.ag", a, "hammer", 1000);
        m1.setNummer(1L);
        Mitglied m2 = new Mitglied("Allvater", "Odin", "odin@asgard.ag", a, "power", 10000);
        m2.setNummer(123L);
        //Mitglied m3 = new Mitglied("Odinsdottir", "Hela", "hela@asgard.ag", a, "zauber", 1000);

        mproxy.createNewMitglied(m1);
        mproxy.createNewMitglied(m2);
        //mproxy.createNewMitglied(m3);
    }
}
