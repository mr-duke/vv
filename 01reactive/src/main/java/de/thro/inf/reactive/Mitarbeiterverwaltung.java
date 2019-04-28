package de.thro.inf.reactive;

import org.apache.log4j.Logger;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


public class Mitarbeiterverwaltung {

    private static final Logger SYSTEM_LOGGER = Logger.getLogger("systemLogger");
    private static final Logger EVENTS_LOGGER = Logger.getLogger("eventLogger");

    private static Mitarbeiterverwaltung mitarbeiterverwaltung = null;
    private List<Mitarbeiter> mitarbeiter;
    private Mitarbeiterverwaltung() throws IOException {
        mitarbeiter = new LinkedList<>();
    }

    public static Mitarbeiterverwaltung getMitarbeiterverwaltung() throws IOException {
        if (mitarbeiterverwaltung == null)
            mitarbeiterverwaltung = new Mitarbeiterverwaltung();
        SYSTEM_LOGGER.debug("Mitarbeiterverwaltung gestartet");
        return mitarbeiterverwaltung;
    }

    public void notify (Ereignis e){
        Mitarbeiter m = filterMitarbeiter(e);
        if (m == null)
            mitarbeiter.add(new Mitarbeiter(e.getMitarbeiterId()));
        else
            m.bewegen(m.getAktuellerZustand(), e.getRichtung());
        EVENTS_LOGGER.info("Mitarbeiter neu angelegt");
    }

    public List<Mitarbeiter> getMitarbeiter() {
        return mitarbeiter;
    }

    private Mitarbeiter filterMitarbeiter(Ereignis e) {
        return mitarbeiter.stream()
                    .filter(mit -> mit.getId().equals(e.getMitarbeiterId()))
                    .findAny()
                    .orElse(null);
    }
}
