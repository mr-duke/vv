package de.thro.vv.kleiderkreisel.server.helpers;

import de.thro.vv.kleiderkreisel.server.entities.Konto;
import de.thro.vv.kleiderkreisel.server.entities.Mitglied;
import de.thro.vv.kleiderkreisel.server.repositories.KontoRepository;
import de.thro.vv.kleiderkreisel.server.repositories.MitgliedRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class GebuehrenRechner {

    // Gebühr pro Tauschvorgang in EuroCent
    private final long TAUSCHGEBUEHR = 50;
    // Handelplattform könnte mehrere Konten besitzen; in diesem Beispiel wird der Einfachheit halber
    // nur über ein und dasselbe Konto abgerechnet
    private final long KONTO_ID = 1;
    private KontoRepository kontoRepo;
    private MitgliedRepository mrepo;

    @Autowired
    public GebuehrenRechner(KontoRepository kontoRepo, MitgliedRepository mrepo) {
        this.kontoRepo = kontoRepo;
        this.mrepo = mrepo;
    }

    public void gebuehrenAbrechnen (Mitglied verkaeufer, Mitglied kaeufer) {
        // Tauschgebühr von beiden Partner abziehen;
        long neuerKontostandVerk = verkaeufer.getKontostand() - TAUSCHGEBUEHR;
        long neuerKontostandKaeuf = kaeufer.getKontostand() - TAUSCHGEBUEHR;
        // Update und Abspeichern der Entities
        verkaeufer.setKontostand(neuerKontostandVerk);
        kaeufer.setKontostand(neuerKontostandKaeuf);
        mrepo.save(verkaeufer);
        mrepo.save(kaeufer);

        // Demo-Konto der Konto zurückgeben bzw falls null, neu anlegen
        Konto hdp;
        hdp = kontoRepo.findById(KONTO_ID).orElse(null);
        if (hdp == null){
            Konto neuesKonto = new Konto(1L, 0, LocalDateTime.now());
            hdp = kontoRepo.save(neuesKonto);
        }
        // Zweimal Tauschgebühr auf das Konto der Plattform gutschreiben
        long neuerKontostandHdp = hdp.getKontostand() + 2*TAUSCHGEBUEHR;
        hdp.setKontostand(neuerKontostandHdp);
        hdp.setZuletztGeaendert(LocalDateTime.now());
        kontoRepo.save(hdp);
    }
}
