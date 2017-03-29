package es.flaviojmend.busyFlights.controller;


import es.flaviojmend.xjet.AppConfiguration;
import es.flaviojmend.xjet.Values;
import es.flaviojmend.xjet.command.ToughJetFilterCommand;
import es.flaviojmend.xjet.persistence.entity.ToughJetFlight;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AppConfiguration.class)
@WebAppConfiguration
@TestPropertySource(locations="classpath:test.properties")
@IntegrationTest("server.port:8001")
public class ToughJetControllerTest {


	@Autowired
	private Values values;

	@Test
	public void searchFlights() {
		ToughJetFlight[] flights = new TestRestTemplate().postForObject("http://localhost:" + values.getServerPort() +"/toughJet",
				new ToughJetFilterCommand(),
				ToughJetFlight[].class);
		assertEquals(30, flights.length);

	}

}
