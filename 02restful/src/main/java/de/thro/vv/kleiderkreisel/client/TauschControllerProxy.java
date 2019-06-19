package de.thro.vv.kleiderkreisel.client;

import de.thro.vv.kleiderkreisel.server.entities.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class TauschControllerProxy {

    // Gebühr pro Tauschvorgang in EuroCent
    private final long TAUSCHGEBUEHR = 50;


    private final String BASE_URI = "http://localhost:8080/";
    private final String VERSION = "api/v1/";

    public Tausch findTauschById (Long id) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Tausch> response = restTemplate.getForEntity(
                BASE_URI + VERSION + "tausch/" + id, Tausch.class);
        if (response.getStatusCode().equals(HttpStatus.OK)){
            return response.getBody();
        } else if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)){
            System.err.println("Tauschvorgang nicht gefunden");
        }
        // Im Fehlerfall immer null zurückgeben
        return null;
    }

    public List<Tausch> findAllTauschvorgaenge() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Tausch>> response = restTemplate.exchange(
                BASE_URI + VERSION + "tausch/", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Tausch>>() {} );

        if (response.getStatusCode().equals(HttpStatus.OK) || response.getStatusCode().equals(HttpStatus.NOT_FOUND)){
            return response.getBody();
        } else {
            // Bei Ausnahmefehlern return null
            return null;
        }
    }

    public Tausch createNewTausch (Mitglied kaeufer, Mitglied verkaeufer, Kleidung kleidung){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        // Tauschgebühr von beiden Partner abziehen;
        long neuerKontostandVerk = verkaeufer.getKontostand() - TAUSCHGEBUEHR;
        long neuerKontostandKaeuf = kaeufer.getKontostand() - TAUSCHGEBUEHR;

        // Update und Abspeichern der Entities
        verkaeufer.setKontostand(neuerKontostandVerk);
        verkaeufer.removeKleidung(kleidung);
        kaeufer.setKontostand(neuerKontostandKaeuf);
        kaeufer.addKleidung(kleidung);

        // Verkaeufer updaten
        HttpEntity<Mitglied> entity = new HttpEntity<>(verkaeufer, httpHeaders);
        ResponseEntity<Mitglied> response = restTemplate.exchange(
                BASE_URI + VERSION + "mitglieder/" + verkaeufer.getNummer(),
                HttpMethod.PUT, entity, Mitglied.class);
        Mitglied verkaeuferUpdated = response.getBody();

        // Kaeufer updaten
        restTemplate = new RestTemplate();
        HttpEntity<Mitglied> entity2 = new HttpEntity<>(kaeufer, httpHeaders);
        ResponseEntity<Mitglied> response2 = restTemplate.exchange(
                BASE_URI + VERSION + "mitglieder/" + kaeufer.getNummer(),
                HttpMethod.PUT, entity2, Mitglied.class);
        Mitglied kaeuferUpdated = response2.getBody();

        // Neuen Tauschvorgang anlegen
        Tausch tausch = new Tausch(LocalDateTime.now(), kleidung.getHersteller());
        tausch.setKaeufer(kaeuferUpdated);
        tausch.setVerkaeufer(verkaeuferUpdated);

        restTemplate = new RestTemplate();
        HttpEntity<Tausch> entity3 = new HttpEntity<>(tausch, httpHeaders);
        ResponseEntity<Tausch> response3 = restTemplate.exchange(
                BASE_URI + VERSION + "tausch",
                HttpMethod.POST, entity3, Tausch.class);

        if (response3.getStatusCode().equals(HttpStatus.CREATED)){
            return response3.getBody();
        } else {
            System.err.println("Fehler beim Anlegen eines neuen Tauschvorgangs");
            // Im Fehlerfall immer null zurückgeben
            return null;
        }
    }

    public Tausch updateTausch (Tausch tausch) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Tausch> entity = new HttpEntity<>(tausch, httpHeaders);
        ResponseEntity<Tausch> response = restTemplate.exchange(
                BASE_URI + VERSION + "tausch/" + tausch.getId(),
                HttpMethod.PUT, entity, Tausch.class);
        if (response.getStatusCode().equals(HttpStatus.OK)){
            return response.getBody();
        } else if (response.getStatusCode().equals(HttpStatus.CONFLICT)){
            System.err.println("Konflikt beim Update aufgetreten");
        } else if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)){
            System.err.println("Tauchvorgang nicht gefunden");
        }
        // Im Fehlerfall immer null zurückgeben
        return null;
    }

    public void deleteTausch (Tausch tausch) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Tausch> entity = new HttpEntity<>(tausch, httpHeaders);
        ResponseEntity<Void> response = restTemplate.exchange(
                BASE_URI + VERSION + "tausch/" + tausch.getId(),
                HttpMethod.DELETE, entity, Void.class);

        // Tauschvorgang erfolgreich gelöscht
        if (response.getStatusCode().equals(HttpStatus.NO_CONTENT)){
            return;
        } else if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)){
            System.err.println("Tauschvorgang nicht gefunden");
        } else if (response.getStatusCode().equals(HttpStatus.BAD_REQUEST)){
            System.err.println("Fehlerhafte Anfrage");
        } else if (response.getStatusCode().equals(HttpStatus.CONFLICT)){
            System.err.println("Konflikt beim Löschen aufgetreten");
        } else {
            System.err.println("Ausnahmefehler");
        }
    }
}
