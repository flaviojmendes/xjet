package es.flaviojmend.busyFlights.controller;


import es.flaviojmend.xjet.AppConfiguration;
import es.flaviojmend.xjet.Values;
import es.flaviojmend.xjet.command.CrazyAirFilterCommand;
import es.flaviojmend.xjet.persistence.entity.CrazyAirFlight;
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
public class CrazyAirControllerTest {

	@Autowired
	private Values values;

	@Test
	public void searchFlights() {
		CrazyAirFlight[] flights = new TestRestTemplate().postForObject("http://localhost:" + values.getServerPort() +"/crazyAir",
				new CrazyAirFilterCommand(),
				CrazyAirFlight[].class);
		assertEquals(30, flights.length);

	}

}
