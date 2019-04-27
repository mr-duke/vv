package de.thro.inf.reactive;

import java.util.LinkedList;
import java.util.List;

public class Mitarbeiterverwaltung {

    private static Mitarbeiterverwaltung mitarbeiterverwaltung = null;
    private List<Mitarbeiter> mitarbeiter;
    private Mitarbeiterverwaltung(){
        mitarbeiter = new LinkedList<>();
    }

    public static Mitarbeiterverwaltung getMitarbeiterverwaltung() {
        if (mitarbeiterverwaltung == null)
            mitarbeiterverwaltung = new Mitarbeiterverwaltung();
        return mitarbeiterverwaltung;
    }

    public void notify (Ereignis e){
        Mitarbeiter m = filterMitarbeiter(e);
        if (m == null)
            mitarbeiter.add(new Mitarbeiter(e.getMitarbeiterId()));
        else
            m.bewegen(m.getAktuellerZustand(), e.getRichtung());
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
