package de.thro.inf.kunde;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1") // Versionierung

// Notiz: localhost:8080/swagger-ui.html
// Notiz: SwaggerConfig

public class KundeService {

    @Autowired // Dependency Injection
    private KundeRepository repo;

    @RequestMapping(
            value = "kunden/{nummer}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Kunde> findKunde(@PathVariable("nummer") String nummer) {
        Kunde k = repo.findById(Long.parseLong(nummer)).orElse(null);
        return new ResponseEntity<>(k, HttpStatus.OK);
    }

    @RequestMapping(
            value = "kunden",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Kunde> createKunde(@RequestBody Kunde kunde) {
        repo.save(kunde);
        return new ResponseEntity<>(kunde, HttpStatus.CREATED);
    }
}

