package es.flaviojmend.xjet.service;

import es.flaviojmend.xjet.command.CrazyAirFilterCommand;
import es.flaviojmend.xjet.persistence.entity.CrazyAirFlight;
import es.flaviojmend.xjet.persistence.repo.CrazyAirRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CrazyAirService {

    @Autowired
    CrazyAirRepository repository;

    public Iterable<CrazyAirFlight> listFlights(CrazyAirFilterCommand crazyAirFilter) {



        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<CrazyAirFlight[]> responseEntity = restTemplate.getForEntity("http://crazyAir:8001/crazyAir", CrazyAirFlight[].class);
        CrazyAirFlight[] flightsAry = responseEntity.getBody();

        List<CrazyAirFlight> flights = Arrays.asList(flightsAry);


        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

        return flights.stream()
                .filter(flight ->
                        crazyAirFilter.getOrigin() == null ||
                        flight.getDepartureAirportCode().equals(crazyAirFilter.getOrigin())
                ).filter(flight ->
                        crazyAirFilter.getDestination() == null ||
                        flight.getDestinationAirportCode().equals(crazyAirFilter.getDestination())
                ).filter(flight ->
                        crazyAirFilter.getDepartureDate() == null ||
                        sdf.format(flight.getDepartureDate()).equals(sdf.format(crazyAirFilter.getDepartureDate()))
                ).filter(flight ->
                        crazyAirFilter.getReturnDate() == null ||
                        sdf.format(flight.getArrivalDate()).equals(sdf.format(crazyAirFilter.getReturnDate()))
                ).collect(Collectors.toList());


    }

    public CrazyAirFlight save(CrazyAirFlight crazyAirFlight) {
        return repository.save(crazyAirFlight);
    }
}
