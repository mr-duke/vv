package de.thro.vv.kleiderkreisel.client;

import de.thro.vv.kleiderkreisel.server.entities.Adresse;
import de.thro.vv.kleiderkreisel.server.entities.Kleidung;
import de.thro.vv.kleiderkreisel.server.entities.Mitglied;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class KleidungControllerProxy {

    private final String BASE_URI = "http://localhost:8080/";
    private final String VERSION = "api/v1/";

    public Kleidung findKleidungById (Long id) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Kleidung> response = restTemplate.getForEntity(
                BASE_URI + VERSION + "kleider/" + id, Kleidung.class);
        if (response.getStatusCode().equals(HttpStatus.OK)){
            return response.getBody();
        } else if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)){
            System.err.println("Kleidungsstück nicht gefunden");
        }
        // Im Fehlerfall immer null zurückgeben
        return null;
    }

    public List<Kleidung> findAllKleider() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Kleidung>> response = restTemplate.exchange(
                BASE_URI + VERSION + "kleider/", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Kleidung>>() {} );

        if (response.getStatusCode().equals(HttpStatus.OK) || response.getStatusCode().equals(HttpStatus.NOT_FOUND)){
            return response.getBody();
        } else {
            // Bei Ausnahmefehlern return null
            return null;
        }
    }

    public Kleidung createNewKleidung (long neupreis, long tauschwert, Kleidung.Kleidergroesse groesse, Kleidung.Geschlecht geschlecht, Kleidung.Typ typ, String hersteller, String foto) {
        Kleidung kleidungNew = new Kleidung(neupreis, tauschwert, groesse, geschlecht, typ, hersteller, foto);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Kleidung> entity = new HttpEntity<>(kleidungNew, httpHeaders);
        // Neues Kleidungsstück erhält Server-generierte ID, daher zuerst passende URI ausgeben lassen
        URI uri = restTemplate.postForLocation(
                BASE_URI + VERSION + "kleider/",
                entity, Kleidung.class);

        ResponseEntity<Kleidung> response = restTemplate.getForEntity(uri, Kleidung.class);
        if (response.getStatusCode().equals(HttpStatus.CREATED)){
            return response.getBody();
        } else {
            System.err.println("Fehler beim Anlegen eines neuen Kleidungsstücks");
            // Im Fehlerfall immer null zurückgeben
            return null;
        }
    }

    public Kleidung updateKleidung (Kleidung kleidung) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Kleidung> entity = new HttpEntity<>(kleidung, httpHeaders);
        ResponseEntity<Kleidung> response = restTemplate.exchange(
                BASE_URI + VERSION + "kleider/" + kleidung.getId(),
                HttpMethod.PUT, entity, Kleidung.class);
        if (response.getStatusCode().equals(HttpStatus.OK)){
            return response.getBody();
        } else if (response.getStatusCode().equals(HttpStatus.CONFLICT)){
            System.err.println("Konflikt beim Update aufgetreten");
        } else if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)){
            System.err.println("Kleidungsstück nicht gefunden");
        }
        // Im Fehlerfall immer null zurückgeben
        return null;
    }

    public void deleteKleidung (Kleidung kleidung) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Kleidung> entity = new HttpEntity<>(kleidung, httpHeaders);
        ResponseEntity<Void> response = restTemplate.exchange(
                BASE_URI + VERSION + "kleider/" + kleidung.getId(),
                HttpMethod.DELETE, entity, Void.class);

        // Kleidungsstück erfolgreich gelöscht
        if (response.getStatusCode().equals(HttpStatus.NO_CONTENT)){
            return;
        } else if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)){
            System.err.println("Kleidungsstück nicht gefunden");
        } else if (response.getStatusCode().equals(HttpStatus.BAD_REQUEST)){
            System.err.println("Fehlerhafte Anfrage");
        } else if (response.getStatusCode().equals(HttpStatus.CONFLICT)){
            System.err.println("Konflikt beim Löschen aufgetreten");
        } else {
            System.err.println("Ausnahmefehler");
        }
    }
}
