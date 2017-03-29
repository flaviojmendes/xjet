package es.flaviojmend.xjet.service;
import es.flaviojmend.xjet.command.ToughJetFilterCommand;
import es.flaviojmend.xjet.persistence.entity.ToughJetFlight;
import es.flaviojmend.xjet.persistence.repo.ToughJetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ToughJetService {

    @Autowired
    ToughJetRepository repository;

    public Iterable<ToughJetFlight> listFlights(ToughJetFilterCommand toughJetFilter) {


        List<ToughJetFlight> flights = new ArrayList<>();
        repository.findAll().forEach(flights::add);

        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

        return flights.stream()
                .filter(flight ->
                        toughJetFilter.getFrom() == null ||
                        flight.getDepartureAirportName().equals(toughJetFilter.getFrom())
                ).filter(flight ->
                        toughJetFilter.getTo() == null ||
                        flight.getArrivalAirportName().equals(toughJetFilter.getTo())
                ).filter(flight ->
                        toughJetFilter.getDepartureDay() == null ||
                                flight.getDepartureDay().equals(toughJetFilter.getDepartureDay())
                ).filter(flight ->
                        toughJetFilter.getDepartureMonth() == null ||
                                flight.getDepartureMonth().equals(toughJetFilter.getDepartureMonth())
                ).filter(flight ->
                        toughJetFilter.getDepartureYear() == null ||
                                flight.getDepartureYear().equals(toughJetFilter.getDepartureYear())
                ).filter(flight ->
                        toughJetFilter.getReturnDay() == null ||
                                flight.getReturnDay().equals(toughJetFilter.getReturnDay())
                ).filter(flight ->
                        toughJetFilter.getReturnMonth() == null ||
                                flight.getReturnMonth().equals(toughJetFilter.getReturnMonth())
                ).filter(flight ->
                        toughJetFilter.getDepartureYear() == null ||
                                flight.getDepartureYear().equals(toughJetFilter.getDepartureYear())
                ).collect(Collectors.toList());


    }

    public ToughJetFlight save(ToughJetFlight toughJetFlight) {
        return repository.save(toughJetFlight);
    }
}
