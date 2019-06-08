package de.thro.vv.kleiderkreisel.server.controller;

import de.thro.vv.kleiderkreisel.server.repositories.MitgliedRepository;
import de.thro.vv.kleiderkreisel.server.entities.Mitglied;
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

@Api( description = "ReST APIs für Klasse Mitglieder",
        produces = MediaType.APPLICATION_JSON_VALUE,
        basePath = "api/v1")
@RestController
@RequestMapping(value = "api/v1")  // Versionierung
public class MitgliedController {

    private MitgliedRepository mrepo;

    @Autowired
    public MitgliedController (MitgliedRepository mrepo){
        this.mrepo = mrepo;
    }

    @ApiOperation(
            value = "Finde Mitglied über ID",
            response = Mitglied.class)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Mitglied gefunden"),
                    @ApiResponse(code = 404, message = "Mitglied nicht gefunden")})
    @RequestMapping(
            value = "mitglieder/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findMitgliedById (@PathVariable("id") String id) {
        Mitglied mitglied = mrepo.findById(Long.parseLong(id)).orElse(null);

        if (mitglied != null) {
            return new ResponseEntity<Mitglied>(mitglied, HttpStatus.OK);
        } else {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
    }


    @ApiOperation(
            value = "Finde alle Mitglieder",
            response = List.class)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Alle Mitglieder aufgelistet"),
                    @ApiResponse(code = 404, message = "Keine Mitglieder gefunden")})
    @RequestMapping(
            value = "mitglieder",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Mitglied>> findAllMitglieder(){
        Iterable<Mitglied> mitgliedIterable = mrepo.findAll();
        List<Mitglied> mitgliederList = new LinkedList<>();
        mitgliedIterable.forEach(mitglied -> mitgliederList.add(mitglied));

        if (mitgliederList.size() == 0){
            // Falls Liste leer, leere Liste zurürckgeben
            return new ResponseEntity<>(mitgliederList, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(mitgliederList, HttpStatus.OK);
        }
    }


    @ApiOperation(
            value = "Lege neues Mitglied an",
            response = Mitglied.class)
    @ApiResponses(
            @ApiResponse(code = 201, message = "Neues Mitglied angelegt")
    )
    @RequestMapping(
            value = "mitglieder",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createNewMitglied (@RequestBody Mitglied mitglied,
                                                UriComponentsBuilder uriComponentsBuilder) {
        Mitglied mitgliedNew = mrepo.save(mitglied);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder
                .path("api/v1/mitglieder/{id}")
                .buildAndExpand(mitgliedNew.getNummer()).toUri());

        return new ResponseEntity<>(mitgliedNew, httpHeaders, HttpStatus.CREATED);
    }


    @ApiOperation(
            value = "Ändere Mitglied",
            response = Mitglied.class)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Mitglied geändert"),
                    @ApiResponse(code = 404, message = "Mitglied nicht gefunden"),
                    @ApiResponse(code = 409, message = "Konflikt")
            })
    @RequestMapping(
            value = "mitglieder/{id}",
            method = {RequestMethod.PUT, RequestMethod.PATCH},
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> modifyMitglied(@PathVariable("id") String id,
                                            @RequestBody Mitglied mitgliedUpdate){
        Mitglied existingMitglied = mrepo.findById(Long.parseLong(id))
                .orElse(null);
        if (existingMitglied == null){
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        if (existingMitglied.getVersion() > mitgliedUpdate.getVersion()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        // ID beibt unverändert
        existingMitglied.setNachname(mitgliedUpdate.getNachname());
        existingMitglied.setVorname(mitgliedUpdate.getVorname());
        existingMitglied.setEmail(mitgliedUpdate.getEmail());
        existingMitglied.setAdresse(mitgliedUpdate.getAdresse());
        existingMitglied.setKontostand(mitgliedUpdate.getKontostand());
        existingMitglied.setPassword(mitgliedUpdate.getPassword());
        existingMitglied.setFoto(mitgliedUpdate.getFoto());
        //existingMitglied.setKleider(mitgliedUpdate.getKleider());
        existingMitglied.setKaeufe(mitgliedUpdate.getKaeufe());
        existingMitglied.setVerkaeufe(mitgliedUpdate.getVerkaeufe());

        Mitglied updatedMitglied = mrepo.save(existingMitglied);

        // Alternative ??
        // 1. Alles auskommentieren
        // 2. mrepo.save(mitgliedUpdate);
        return new ResponseEntity<>(updatedMitglied, HttpStatus.OK);
    }


    @ApiOperation(
            value = "Lösche ein Mitglied",
            response = Mitglied.class)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 204, message = "Mitglied gelöscht"),
                    @ApiResponse(code = 404, message = "Mitglied nicht gefunden"),
                    @ApiResponse(code = 409, message = "Konflikt"),
                    @ApiResponse(code = 400, message = "Fehlerhafte Anfrage")
            })
    @RequestMapping(
            value = "mitglieder/{id}",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> deleteMitglied (@PathVariable("id") String id,
                                                @RequestBody Mitglied mitglied){
        Mitglied existingMitglied = mrepo.findById(Long.parseLong(id)).orElse(null);
        if (existingMitglied == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(existingMitglied.getVersion() > mitglied.getVersion()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if(!existingMitglied.getNummer().equals(mitglied.getNummer())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        mrepo.delete(existingMitglied);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
