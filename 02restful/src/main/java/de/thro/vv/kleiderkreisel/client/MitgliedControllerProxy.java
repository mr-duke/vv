package de.thro.vv.kleiderkreisel.client;

import de.thro.vv.kleiderkreisel.server.entities.Adresse;
import de.thro.vv.kleiderkreisel.server.entities.Mitglied;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MitgliedControllerProxy {

    private final String BASE_URI = "http://localhost:8080/";
    private final String VERSION = "api/v1/";

    public Mitglied findMitgliedById (Long id) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Mitglied> response = restTemplate.getForEntity(
                BASE_URI + VERSION + "mitglieder/" + id, Mitglied.class);
        if (response.getStatusCode().equals(HttpStatus.OK)){
            return response.getBody();
        } else if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)){
            System.err.println("Mitglied nicht gefunden");
        }
        // Im Fehlerfall immer null zurückgeben
        return null;
    }

    public List<Mitglied> findAllMitglieder() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Mitglied>> response = restTemplate.exchange(
                BASE_URI + VERSION + "mitglieder/", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Mitglied>>() {} );

        if (response.getStatusCode().equals(HttpStatus.OK)){
            return response.getBody();
        } else {
            // Leere Liste falls keine Mitglieder vorhanden
            return new LinkedList<>();
        }
    }

    public Mitglied createNewMitglied (String nachname, String vorname, String email, Adresse adresse, String foto, String password, long kontostand) {
        Mitglied mitgliedNew = new Mitglied(nachname, vorname, email, adresse, foto, password, kontostand);

        RestTemplate restTemplate = new RestTemplate();
        URI uri = restTemplate.postForLocation(
                BASE_URI + VERSION + "mitglieder/",
                mitgliedNew, Mitglied.class);

        ResponseEntity<Mitglied> response = restTemplate.getForEntity(uri, Mitglied.class);
        if (response.getStatusCode().equals(HttpStatus.CREATED)){
            return response.getBody();
        } else {
            System.err.println("Fehler beim Anlegen eines neuen Mitglieds");
            // Im Fehlerfall immer null zurückgeben
            return null;
        }
    }

    public Mitglied updateMitglied (Mitglied mitglied) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Mitglied> entity = new HttpEntity<>(mitglied, httpHeaders);
        ResponseEntity<Mitglied> response = restTemplate.exchange(
                BASE_URI + VERSION + "mitglieder/" + mitglied.getNummer(),
                HttpMethod.PUT, entity, Mitglied.class);
        if (response.getStatusCode().equals(HttpStatus.OK)){
            return response.getBody();
        } else if (response.getStatusCode().equals(HttpStatus.CONFLICT)){
            System.err.println("Konflikt beim Update aufgetreten");
        } else if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)){
            System.err.println("Mitglied nicht gefunden");
        }
        // Im Fehlerfall immer null zurückgeben
        return null;
    }

    public void deleteMitglied (Mitglied mitglied) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Mitglied> entity = new HttpEntity<>(mitglied, httpHeaders);
        ResponseEntity<Void> response = restTemplate.exchange(
                BASE_URI + VERSION + "mitglieder/" + mitglied.getNummer(),
                HttpMethod.DELETE, entity, Void.class);

        // Mitglied erfolgreich gelöscht
        if (response.getStatusCode().equals(HttpStatus.NO_CONTENT)){
            return;
        } else if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)){
            System.err.println("Mitglied nicht gefunden");
        } else if (response.getStatusCode().equals(HttpStatus.BAD_REQUEST)){
            System.err.println("Fehlerhafte Anfrage");
        } else if (response.getStatusCode().equals(HttpStatus.CONFLICT)){
            System.err.println("Konflikt beim Löschen aufgetreten");
        } else {
            System.err.println("Ausnahmefehler");
        }
    }
}
