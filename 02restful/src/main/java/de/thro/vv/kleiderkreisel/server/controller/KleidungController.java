package de.thro.vv.kleiderkreisel.server.controller;

import de.thro.vv.kleiderkreisel.server.repositories.KleidungRepository;
import de.thro.vv.kleiderkreisel.server.entities.Kleidung;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedList;
import java.util.List;

@Api( description = "ReST APIs für Klasse Kleidung",
        produces = MediaType.APPLICATION_JSON_VALUE,
        basePath = "api/v1")
@RestController
@RequestMapping(value = "api/v1")  // Versionierung
public class KleidungController {

    private KleidungRepository krepo;

    @Autowired
    public KleidungController (KleidungRepository krepo){
        this.krepo = krepo;
    }

    @ApiOperation(
            value = "Finde Kleidungsstück über ID",
            response = Kleidung.class)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Kleidungsstück gefunden"),
                    @ApiResponse(code = 404, message = "Kleidungsstück nicht gefunden")})
    @RequestMapping(
            value = "kleider/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findKleidungById (@PathVariable("id") String id) {
        Kleidung kleidung = krepo.findById(Long.parseLong(id)).orElse(null);

        if (kleidung != null) {
            return new ResponseEntity<Kleidung>(kleidung, HttpStatus.OK);
        } else {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
    }


    @ApiOperation(
            value = "Finde alle Kleidungsstücke",
            response = List.class)
    @ApiResponses(
            @ApiResponse(code = 200, message = "Alle Kleidungsstücke aufgelistet")
    )
    @RequestMapping(
            value = "kleider",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Kleidung>> findAllKleider(){
        Iterable<Kleidung> kleidungIterable = krepo.findAll();
        List<Kleidung> kleiderList = new LinkedList<>();
        kleidungIterable.forEach(kleidung -> kleiderList.add(kleidung));

        return new ResponseEntity<>(kleiderList, HttpStatus.OK);
    }


    @ApiOperation(
            value = "Lege neues Kleidungsstück an",
            response = Kleidung.class)
    @ApiResponses(
            @ApiResponse(code = 201, message = "Neues Kleidungsstück angelegt")
    )
    @RequestMapping(
            value = "kleider",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createNewKleidung (@RequestBody Kleidung kleidung,
                                                UriComponentsBuilder uriComponentsBuilder) {
        krepo.save(kleidung);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder
                .path("api/v1/kleider/{id}")
                .buildAndExpand(kleidung.getId()).toUri());
        return new ResponseEntity<String>(httpHeaders, HttpStatus.CREATED);
    }


    @ApiOperation(
            value = "Ändere Kleidungsstück",
            response = Kleidung.class)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Kleidungsstück geändert"),
                    @ApiResponse(code = 404, message = "Kleidungsstück nicht gefunden"),
                    @ApiResponse(code = 409, message = "Konflikt")
            })
    @RequestMapping(
            value = "kleider/{id}",
            method = {RequestMethod.PUT, RequestMethod.PATCH},
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> modifyKleidung(@PathVariable("id") String id,
                                            @RequestBody Kleidung kleidungUpdate){
        Kleidung existingKleidung = krepo.findById(Long.parseLong(id))
                .orElse(null);
        if (existingKleidung == null){
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        if (existingKleidung.getVersion() > kleidungUpdate.getVersion()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        // ID beibt unverändert
        existingKleidung.setNeupreis(kleidungUpdate.getNeupreis());
        existingKleidung.setTauschwert(kleidungUpdate.getTauschwert());
        existingKleidung.setGroesse(kleidungUpdate.getGroesse());
        existingKleidung.setGeschlecht(kleidungUpdate.getGeschlecht());
        existingKleidung.setTyp(kleidungUpdate.getTyp());
        existingKleidung.setHersteller(kleidungUpdate.getHersteller());
        existingKleidung.setFoto(kleidungUpdate.getFoto());

        krepo.save(existingKleidung);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @ApiOperation(
            value = "Lösche ein Kleidungsstück",
            response = Kleidung.class)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 202, message = "Kleidungsstück gelöscht"),
                    @ApiResponse(code = 404, message = "Kleidungsstück nicht gefunden"),
                    @ApiResponse(code = 409, message = "Konflikt"),
                    @ApiResponse(code = 400, message = "Fehlerhafte Anfrage")
            })
    @RequestMapping(
            value = "kleider/{id}",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> deleteKleidung (@PathVariable("id") String id,
                                                @RequestBody Kleidung kleidung){
        Kleidung existingKleidung = krepo.findById(Long.parseLong(id)).orElse(null);
        if (existingKleidung == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(existingKleidung.getVersion() > kleidung.getVersion()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if(!existingKleidung.getId().equals(kleidung.getId())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        krepo.delete(existingKleidung);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
