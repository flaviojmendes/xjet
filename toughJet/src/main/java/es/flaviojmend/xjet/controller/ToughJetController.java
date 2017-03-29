package es.flaviojmend.xjet.controller;

import es.flaviojmend.xjet.command.ToughJetFilterCommand;
import es.flaviojmend.xjet.persistence.entity.ToughJetFlight;
import es.flaviojmend.xjet.service.ToughJetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/toughJet")
public class ToughJetController {


    @Autowired
    ToughJetService toughJetService;

    @RequestMapping(method = RequestMethod.POST)
    public Iterable<ToughJetFlight> search(@RequestBody ToughJetFilterCommand toughJetFilterCommand) {
        return toughJetService.listFlights(toughJetFilterCommand);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<ToughJetFlight> listAll() {

        return toughJetService.listFlights(new ToughJetFilterCommand());
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> save(@RequestBody ToughJetFlight toughJetFlight) {
        HttpHeaders httpHeaders = new HttpHeaders();
        return new ResponseEntity<>(toughJetService.save(toughJetFlight), httpHeaders, HttpStatus.OK);
    }




}