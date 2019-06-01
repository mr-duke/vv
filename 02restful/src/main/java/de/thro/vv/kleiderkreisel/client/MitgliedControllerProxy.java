package de.thro.vv.kleiderkreisel.client;

import de.thro.vv.kleiderkreisel.server.entities.Adresse;
import de.thro.vv.kleiderkreisel.server.entities.Mitglied;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
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
        } else {
            return null;
        }
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
        if (response.getStatusCode().equals(HttpStatus.OK)){
            return response.getBody();
        } else {
            return null;
        }
    }


}
