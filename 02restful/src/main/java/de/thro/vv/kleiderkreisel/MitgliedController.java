package de.thro.vv.kleiderkreisel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.web.scanners.MediaTypeReader;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

@Api( description = "ReST APIs für Klasse Mitglieder",
        produces = MediaType.APPLICATION_JSON_VALUE,
        basePath = "api/v1")
@RestController
@RequestMapping(value = "api/v1")
public class MitgliedController {

    private MitgliedRepository mrepo;
    @Autowired
    public MitgliedController (MitgliedRepository mrepo){
        this.mrepo = mrepo;
    }

    @ApiOperation( value = "Finde Mitglied nach ID",
            response = Mitglied.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Mitglied gefunden"),
            @ApiResponse(code = 404, message = "Mitglied nicht gefunden")})
    @RequestMapping( value = "mitglieder/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findMitgliedById (@PathVariable("id") String id) {
        Mitglied mitglied = mrepo.findById(Long.parseLong(id)).orElse(null);

        if (mitglied != null) {
            return new ResponseEntity<Mitglied>(mitglied, HttpStatus.OK);
        } else {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
    }

    // List<Mitglied>.class nicht möglich -> Swagger zeigt in Vorschau leere Liste an
    @ApiOperation(value = "Finde alle Mitglieder", response = List.class)
    @ApiResponses(
            @ApiResponse(code = 200, message = "Alles Mitglieder aufgelistet")
    )
    @RequestMapping(value = "mitglieder",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Mitglied>> findAllMitglieder(){
        Iterable<Mitglied> mitgliedIterable = mrepo.findAll();
        List<Mitglied> mitglieder = new LinkedList<>();
        mitgliedIterable.forEach(mitglied -> mitglieder.add(mitglied));

    return new ResponseEntity<>(mitglieder, HttpStatus.OK);
    }


}
