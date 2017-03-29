package es.flaviojmend.xjet.controller;

import es.flaviojmend.xjet.command.CrazyAirFilterCommand;
import es.flaviojmend.xjet.command.FlightsFilterCommand;
import es.flaviojmend.xjet.InvalidQuantityException;
import es.flaviojmend.xjet.persistence.entity.CrazyAirFlight;
import es.flaviojmend.xjet.service.SearchFlightsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/searchFlights")
public class FlightsController {

    @Autowired
    private SearchFlightsService searchFlightsService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> search(@RequestBody FlightsFilterCommand flightsFilterCommand) {
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            return new ResponseEntity<>(searchFlightsService.listFlights(flightsFilterCommand), httpHeaders, HttpStatus.OK);
        } catch (InvalidQuantityException e) {
            return new ResponseEntity<>(e.getMessage(), httpHeaders, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> listAll() {
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            return new ResponseEntity<>(searchFlightsService.listFlights(new FlightsFilterCommand()), httpHeaders, HttpStatus.OK);
        } catch (InvalidQuantityException e) {
            return new ResponseEntity<>(e.getMessage(), httpHeaders, HttpStatus.BAD_REQUEST);
        }
    }

}
