package de.thro.vv.kleiderkreisel.server.controller;

import de.thro.vv.kleiderkreisel.server.repositories.TauschRepository;
import de.thro.vv.kleiderkreisel.server.entities.Tausch;
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

    @ApiOperation(
            value = "Finde Tauschvorgang über ID",
            response = Tausch.class)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Tauschvorgang gefunden"),
                    @ApiResponse(code = 404, message = "Tauschvorgang nicht gefunden")})
    @RequestMapping(
            value = "tausch/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findTauschById (@PathVariable("id") String id) {
        Tausch tausch = trepo.findById(Long.parseLong(id)).orElse(null);

        if (tausch != null) {
            return new ResponseEntity<Tausch>(tausch, HttpStatus.OK);
        } else {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
    }


    @ApiOperation(
            value = "Finde alle Tauschvorgänge",
            response = List.class)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Alle Tauschvorgänge aufgelistet"),
                    @ApiResponse(code = 404, message = "Keine Tauschvorgänge gefunden")})
    @RequestMapping(
            value = "tausch",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Tausch>> findAllTauschvorgaenge(){
        Iterable<Tausch> tauschIterable = trepo.findAll();
        List<Tausch> tauschvorgaengeList = new LinkedList<>();
        tauschIterable.forEach(tausch -> tauschvorgaengeList.add(tausch));

        if (tauschvorgaengeList.size() == 0){
            // Falls Liste leer, leere Liste zurürckgeben
            return new ResponseEntity<>(tauschvorgaengeList, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(tauschvorgaengeList, HttpStatus.OK);
        }
    }


    @ApiOperation(
            value = "Lege neuen Tauschvorgang an",
            response = Tausch.class)
    @ApiResponses(
            @ApiResponse(code = 201, message = "Neuer Tauschvorgang angelegt")
    )
    @RequestMapping(
            value = "tausch",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createNewTausch (@RequestBody Tausch tausch,
                                                UriComponentsBuilder uriComponentsBuilder) {
        trepo.save(tausch);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder
                .path("api/v1/tausch/{id}")
                .buildAndExpand(tausch.getId()).toUri());
        return new ResponseEntity<String>(httpHeaders, HttpStatus.CREATED);
    }


    @ApiOperation(
            value = "Ändere Tauschvorgang",
            response = Tausch.class)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Tauschvorgang geändert"),
                    @ApiResponse(code = 404, message = "Tauschvorgang nicht gefunden"),
                    @ApiResponse(code = 409, message = "Konflikt")
            })
    @RequestMapping(
            value = "tausch/{id}",
            method = {RequestMethod.PUT, RequestMethod.PATCH},
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> modifyTausch(@PathVariable("id") String id,
                                            @RequestBody Tausch tauschUpdate){
        Tausch existingTausch = trepo.findById(Long.parseLong(id))
                .orElse(null);
        if (existingTausch == null){
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        if (existingTausch.getVersion() > tauschUpdate.getVersion()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        // ID beibt unverändert
        existingTausch.setTauschdatum(tauschUpdate.getTauschdatum());

        trepo.save(existingTausch);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @ApiOperation(
            value = "Lösche einen Tauschvorgang",
            response = Tausch.class)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 204, message = "Tauschvorgang gelöscht"),
                    @ApiResponse(code = 404, message = "Tauschvorgang nicht gefunden"),
                    @ApiResponse(code = 409, message = "Konflikt"),
                    @ApiResponse(code = 400, message = "Fehlerhafte Anfrage")
            })
    @RequestMapping(
            value = "tausch/{id}",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> deleteTausch (@PathVariable("id") String id,
                                                @RequestBody Tausch tausch){
        Tausch existingTausch = trepo.findById(Long.parseLong(id)).orElse(null);
        if (existingTausch == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(existingTausch.getVersion() > tausch.getVersion()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if(!existingTausch.getId().equals(tausch.getId())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        trepo.delete(existingTausch);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
