package es.flaviojmend.xjet.controller;

import es.flaviojmend.xjet.command.CrazyAirFilterCommand;
import es.flaviojmend.xjet.persistence.entity.CrazyAirFlight;
import es.flaviojmend.xjet.service.CrazyAirService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crazyAir")
public class CrazyAirController {


    @Autowired
    CrazyAirService crazyAirService;

    @RequestMapping(method = RequestMethod.POST)
    public Iterable<CrazyAirFlight> search(@RequestBody CrazyAirFilterCommand crazyAirFilterCommand) {

        return crazyAirService.listFlights(crazyAirFilterCommand);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> save(@RequestBody CrazyAirFlight crazyAirFlight) {
        HttpHeaders httpHeaders = new HttpHeaders();
        return new ResponseEntity<>(crazyAirService.save(crazyAirFlight), httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<CrazyAirFlight> listAll() {

        return crazyAirService.listFlights(new CrazyAirFilterCommand());
    }

}