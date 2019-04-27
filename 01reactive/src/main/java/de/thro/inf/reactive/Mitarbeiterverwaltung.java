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
        if (mitarbeiter.isEmpty()){
            mitarbeiter.add(new Mitarbeiter(e.getMitarbeiterId()));
            return;
        }

        for(Mitarbeiter m : mitarbeiter) {
            if (m.getId().equals(e.getMitarbeiterId())) {
                m.bewegen(m.getAktuellerZustand(), e.getRichtung());
                return;
            }
        }
        mitarbeiter.add(new Mitarbeiter(e.getMitarbeiterId()));

    }

    public List<Mitarbeiter> getMitarbeiter() {
        return mitarbeiter;
    }
}
