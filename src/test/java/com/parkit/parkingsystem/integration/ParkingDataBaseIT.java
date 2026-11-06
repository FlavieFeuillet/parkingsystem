package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static ParkingSpotDAO parkingSpotDAO;
	private static TicketDAO ticketDAO;
	private static DataBasePrepareService dataBasePrepareService;

	@Mock
	private static InputReaderUtil inputReaderUtil;

	@BeforeAll
	private static void setUp() throws Exception {
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
	}

	@BeforeEach
	private void setUpPerTest() throws Exception {
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		dataBasePrepareService.clearDataBaseEntries();
	}

	@AfterAll
	private static void tearDown() {
	}

	@Test
	public void testParkingACar() {
		// GIVEN
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		// WHEN
		parkingService.processIncomingVehicle();
		// THEN
		Ticket ticket = ticketDAO.getTicket("ABCDEF");
		assertNotNull(ticket);
		assertEquals("ABCDEF", ticket.getVehicleRegNumber());
		assertEquals(0.0, ticket.getPrice(), 0.01);
		assertNotNull(ticket.getInTime());
		assertNull(ticket.getOutTime());
		assertNotNull(ticket.getParkingSpot());
		assertFalse(ticket.getParkingSpot().isAvailable());
	}

	@Test
	public void testParkingLotExit() throws Exception {
		// GIVEN
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		// WHEN
		parkingService.processIncomingVehicle();
		Thread.sleep(1000);
		parkingService.processExitingVehicle();
		Ticket ticket = ticketDAO.getTicket("ABCDEF");
		// THEN
		assertNotNull(ticket);
		assertNotNull(ticket.getOutTime());
		assertNotNull(ticket.getInTime());
		assertTrue(ticket.getOutTime().getTime() >= ticket.getInTime().getTime());
		assertTrue(ticket.getPrice() >= 0);
	}

	@Test
	public void testParkingLotExitRecurringUser() throws Exception {
		// GIVEN
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		// WHEN
		// Fist visit
		parkingService.processIncomingVehicle();
		Thread.sleep(1000);
		parkingService.processExitingVehicle();

		Ticket firstTicket = ticketDAO.getTicket("ABCDEF");
		double firstPrice = firstTicket.getPrice();
		// Second visit
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		parkingService.processIncomingVehicle();
		Thread.sleep(1000);
		parkingService.processExitingVehicle();

		Ticket secondTicket = ticketDAO.getTicket("ABCDEF");
		// THEN
		assertNotNull(secondTicket);
		assertTrue(secondTicket.getPrice() >= 0);
		assertNotNull(secondTicket.getOutTime());
		assertNotNull(secondTicket.getInTime());
		if (firstPrice > 0 && secondTicket.getPrice() > 0) {
			assertTrue(secondTicket.getPrice() <= firstPrice);
		}
	}
}
