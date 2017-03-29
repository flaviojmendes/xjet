package es.flaviojmend.xjet.service;

import es.flaviojmend.xjet.InvalidQuantityException;
import es.flaviojmend.xjet.Values;
import es.flaviojmend.xjet.command.CrazyAirFilterCommand;
import es.flaviojmend.xjet.command.FlightsFilterCommand;
import es.flaviojmend.xjet.command.ToughJetFilterCommand;
import es.flaviojmend.xjet.persistence.entity.CrazyAirFlight;
import es.flaviojmend.xjet.persistence.entity.Flight;
import es.flaviojmend.xjet.persistence.entity.ToughJetFlight;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.logging.Logger;

@Service
public class SearchFlightsService {

    Logger logger = Logger.getLogger(this.toString());

    @Autowired
    private Values values;

    public Iterable<Flight> listFlights(FlightsFilterCommand flightsFilterCommand) throws InvalidQuantityException {

        if(flightsFilterCommand.getNumberOfPassengers() != null && flightsFilterCommand.getNumberOfPassengers() == 0) {
            throw new InvalidQuantityException("The number of passengers must be greater than 0.");
        }
        if(flightsFilterCommand.getNumberOfPassengers() != null && flightsFilterCommand.getNumberOfPassengers() > 4) {
            throw new InvalidQuantityException("The number of passengers must be 4 or less.");
        }


        List<CrazyAirFlight> crazyAirFlights = Arrays.asList(listCrazyAirFlights(flightsFilterCommand));
        List<ToughJetFlight> toughJetFlights = Arrays.asList(listToughJetFlights(flightsFilterCommand));

        return convertFlights(crazyAirFlights, toughJetFlights);
    }

    private List<Flight> convertFlights(List<CrazyAirFlight> crazyAirFlights, List<ToughJetFlight> toughJetFlights) {

        List<Flight> flights = new ArrayList<>();

        crazyAirFlights.forEach(crazyAirFlight ->
                flights.add(new Flight()
                        .setAirline(crazyAirFlight.getAirline())
                .setDestinationAirportCode(crazyAirFlight.getDestinationAirportCode())
                .setDepartureAirportCode(crazyAirFlight.getDepartureAirportCode())
                .setArrivalDate(crazyAirFlight.getArrivalDate())
                .setDepartureDate(crazyAirFlight.getDepartureDate())
                .setFare(crazyAirFlight.getPrice())
                .setSupplier("CrazyAir")
                ));

        toughJetFlights.forEach(toughJetFlight ->
                flights.add(new Flight()
                    .setAirline(toughJetFlight.getCarrier())
                .setDepartureAirportCode(toughJetFlight.getDepartureAirportName())
                .setDestinationAirportCode(toughJetFlight.getArrivalAirportName())
                .setFare(getFare(toughJetFlight))
                .setDepartureDate(getDate(toughJetFlight.getDepartureDay(), toughJetFlight.getDepartureMonth(), toughJetFlight.getDepartureYear()))
                .setArrivalDate(getDate(toughJetFlight.getReturnDay(), toughJetFlight.getReturnMonth(), toughJetFlight.getReturnYear()))
                .setSupplier("ToughJet")
                ));

        flights.sort((o1, o2) -> Double.compare(o1.getFare(), o2.getFare()));

        return flights;
    }

    private Date getDate(Integer day, Integer month, Integer year) {
        Calendar date = new GregorianCalendar();

        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        date.set(Calendar.DAY_OF_MONTH, day);
        date.set(Calendar.MONTH, month);
        date.set(Calendar.YEAR, year);

        return date.getTime();

    }

    private double getFare(ToughJetFlight toughJetFlight) {
        return (toughJetFlight.getBasePrice() + toughJetFlight.getTax()) * toughJetFlight.getDiscount() / 100;
    }

    private CrazyAirFlight[] listCrazyAirFlights(FlightsFilterCommand flightsFilterCommand) {

        logger.info("Searching for CrazyAir flights..");

        CrazyAirFilterCommand crazyAirFilterCommand = new CrazyAirFilterCommand();
        crazyAirFilterCommand.setDepartureDate(flightsFilterCommand.getDepartureDate())
                .setReturnDate(flightsFilterCommand.getReturnDate())
                .setDestination(flightsFilterCommand.getDestination())
                .setNumberOfPassengers(flightsFilterCommand.getNumberOfPassengers())
                .setOrigin(flightsFilterCommand.getOrigin());

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        CrazyAirFlight[] flights = restTemplate.postForObject("http://crazyAir:8001" +"/crazyAir",
                crazyAirFilterCommand,
                CrazyAirFlight[].class);

        logger.info(flights.length + " flights found.");
        return flights;
    }

    private ToughJetFlight[] listToughJetFlights(FlightsFilterCommand flightsFilterCommand) {

        logger.info("Searching for ToughJetFlights..");

        Calendar departureCalendar = null;
        if(flightsFilterCommand.getDepartureDate() != null) {
            departureCalendar = Calendar.getInstance();
            departureCalendar.setTime(flightsFilterCommand.getDepartureDate());
        }

        Calendar returnCalendar = null;
        if(flightsFilterCommand.getReturnDate() != null) {
            returnCalendar = Calendar.getInstance();
            returnCalendar.setTime(flightsFilterCommand.getReturnDate());
        }

        ToughJetFilterCommand toughJetFilterCommand = new ToughJetFilterCommand();
        toughJetFilterCommand.setFrom(flightsFilterCommand.getOrigin())
                .setTo(flightsFilterCommand.getDestination())
                .setDepartureDay(departureCalendar != null ? departureCalendar.get(Calendar.DAY_OF_MONTH) : null)
                .setDepartureMonth(departureCalendar != null ? departureCalendar.get(Calendar.MONTH) : null)
                .setDepartureYear(departureCalendar != null ? departureCalendar.get(Calendar.YEAR) : null)
                .setReturnDay(returnCalendar != null ? returnCalendar.get(Calendar.DAY_OF_MONTH) : null)
                .setReturnMonth(returnCalendar != null ? returnCalendar.get(Calendar.MONTH) : null)
                .setReturnYear(returnCalendar != null ? returnCalendar.get(Calendar.YEAR) : null)
                .setNumberOfAdults(flightsFilterCommand.getNumberOfPassengers());

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());


        ToughJetFlight[] flights = restTemplate.postForObject("http://toughJet:8002" + "/toughJet",
                toughJetFilterCommand,
                ToughJetFlight[].class);

        logger.info(flights.length + " flights found.");

        return flights;

    }
}
