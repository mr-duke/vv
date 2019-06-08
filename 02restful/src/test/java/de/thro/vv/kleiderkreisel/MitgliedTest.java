package de.thro.vv.kleiderkreisel;

import de.thro.vv.kleiderkreisel.client.MitgliedControllerProxy;
import de.thro.vv.kleiderkreisel.server.entities.Adresse;
import de.thro.vv.kleiderkreisel.server.entities.Mitglied;
import org.junit.Test;
import org.springframework.web.client.RestClientException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MitgliedTest {

    @Test(expected = RestClientException.class)
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
}
