package de.thro.vv.kleiderkreisel;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api( description = "ReST APIs für Klasse Tausch",
        produces = MediaType.APPLICATION_JSON_VALUE,
        basePath = "api/v1")
@RestController
@RequestMapping(value = "api/v1")  // Versionierung
public class TauschController {

    private TauschRepository trepo;

    @Autowired
    public TauschController (TauschRepository trepo) {
        this.trepo = trepo;
    }

    
}
