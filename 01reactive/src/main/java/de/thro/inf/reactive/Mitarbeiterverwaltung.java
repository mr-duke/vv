package de.thro.inf.reactive;

import org.apache.log4j.Logger;
import java.util.LinkedList;
import java.util.List;


public class Mitarbeiterverwaltung {

    private static final Logger SYSTEM_LOGGER = Logger.getLogger("systemLogger");
    private static final Logger EVENTS_LOGGER = Logger.getLogger("eventLogger");

    private static Mitarbeiterverwaltung mitarbeiterverwaltung = null;
    private List<Mitarbeiter> mitarbeiter;
    private Mitarbeiterverwaltung() {
        mitarbeiter = new LinkedList<>();
    }

    public static Mitarbeiterverwaltung getMitarbeiterverwaltung() {
        if (mitarbeiterverwaltung == null)
            mitarbeiterverwaltung = new Mitarbeiterverwaltung();
        SYSTEM_LOGGER.info("Mitarbeiterverwaltung gestartet");
        return mitarbeiterverwaltung;
    }

    public void notify (Ereignis e){
        Mitarbeiter m = filterMitarbeiter(e);
        String mitarbeiterID = m.getId();

        EVENTS_LOGGER.info(String.format("Mitarbeiter (%s) geht %s", mitarbeiterID, e.getRichtung()));
        try {
            m.bewegen(m.getAktuellerZustand(), e.getRichtung());
        }
        catch (IllegalArgumentException ex) {
            EVENTS_LOGGER.error(String.format("Mitarbeiter (%s) %s", mitarbeiterID, ex.getMessage()));
        }
        EVENTS_LOGGER.info(String.format("Mitarbeiter (%s) aktueller Zustand: %s", mitarbeiterID, m.getAktuellerZustand()));

    }

    public List<Mitarbeiter> getMitarbeiter() {
        return mitarbeiter;
    }

    private Mitarbeiter filterMitarbeiter(Ereignis e) {
        String mitarbeiterId = e.getMitarbeiterId();
        Mitarbeiter m = mitarbeiter.stream()
                .filter(mit -> mit.getId().equals(mitarbeiterId))
                .findAny()
                .orElse(null);

        if (m == null) {
            m = new Mitarbeiter(mitarbeiterId);
            EVENTS_LOGGER.info(String.format("Mitarbeiter mit ID %s neu angelegt", mitarbeiterId));
            mitarbeiter.add(m);
        }
        return m;
    }
}
