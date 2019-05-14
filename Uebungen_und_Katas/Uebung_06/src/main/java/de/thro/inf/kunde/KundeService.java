package de.thro.inf.kunde;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class KundeService {

    @RequestMapping(value = "kunden/{nummer}",
            method = RequestMethod.GET)
    public Kunde huhu (@PathVariable("nummer") String ich) {
        return new Kunde(Long.parseLong(ich), "Harry", "Hueller");
    }


    @RequestMapping(value = "kunden",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public Kunde huhu2 (@RequestBody Kunde kunde) {
        kunde.setName("Thor");
        return kunde;
    }
}

