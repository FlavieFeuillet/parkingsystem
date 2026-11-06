package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

	private static ParkingService parkingService;

	@Mock
	private static InputReaderUtil inputReaderUtil;
	@Mock
	private static ParkingSpotDAO parkingSpotDAO;
	@Mock
	private static TicketDAO ticketDAO;

	@BeforeEach
	private void setUpPerTest() {
		try {
			parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to set up test mock objects");
		}
	}

	@Test
	public void processExitingVehicleTest() throws Exception {
		// GIVEN
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		Ticket ticket = new Ticket();
		ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCDEF");

		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		when(ticketDAO.getTicket("ABCDEF")).thenReturn(ticket);
		when(ticketDAO.getNbTicket("ABCDEF")).thenReturn(0);
		when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
		when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
		// WHEN
		parkingService.processExitingVehicle();
		// THEN
		verify(ticketDAO, times(1)).getTicket("ABCDEF");
		verify(ticketDAO, times(1)).getNbTicket("ABCDEF");
		verify(ticketDAO, times(1)).updateTicket(argThat(t -> "ABCDEF".equals(t.getVehicleRegNumber())
				&& t.getOutTime() != null && t.getOutTime().after(t.getInTime()) && t.getPrice() > 0));
		verify(parkingSpotDAO, times(1)).updateParking(
				argThat(spot -> spot.getId() == 1 && spot.isAvailable() && spot.getParkingType() == ParkingType.CAR));
	}

	@Test
	public void testProcessIncomingVehicle() throws Exception {
		// GIVEN
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);
		when(ticketDAO.getNbTicket("ABCDEF")).thenReturn(0);
		// WHEN
		parkingService.processIncomingVehicle();
		// THEN
		verify(parkingSpotDAO, times(1)).getNextAvailableSlot(ParkingType.CAR);
		verify(ticketDAO, times(1)).getNbTicket("ABCDEF");
		verify(parkingSpotDAO, times(1)).updateParking(argThat(spot -> spot.getId() == 1 && !spot.isAvailable()));
		verify(ticketDAO, times(1)).saveTicket(argThat(ticket -> "ABCDEF".equals(ticket.getVehicleRegNumber())
				&& ticket.getInTime() != null && ticket.getOutTime() == null && ticket.getParkingSpot().getId() == 1));
		verify(parkingSpotDAO).updateParking(
				argThat(spot -> spot.getId() == 1 && !spot.isAvailable() && spot.getParkingType() == ParkingType.CAR));
	}

	@Test
	public void processExitingVehicleTestUnableUpdate() throws Exception {
		// GIVEN
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		Ticket ticket = new Ticket();
		ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCDEF");

		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		when(ticketDAO.getTicket("ABCDEF")).thenReturn(ticket);
		when(ticketDAO.getNbTicket("ABCDEF")).thenReturn(0);
		when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false);
		// WHEN
		parkingService.processExitingVehicle();
		// THEN
		verify(parkingSpotDAO, never()).updateParking(any(ParkingSpot.class));
	}

	@Test
	public void testGetNextParkingNumberIfAvailable() throws Exception {
		// GIVEN
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);
		// WHEN
		ParkingSpot parkingSpot = parkingService.getNextParkingNumberIfAvailable();
		// THEN
		verify(inputReaderUtil, times(1)).readSelection();
		verify(parkingSpotDAO, times(1)).getNextAvailableSlot(ParkingType.CAR);

		assertNotNull(parkingSpot);
		assertEquals(1, parkingSpot.getId());
		assertEquals(ParkingType.CAR, parkingSpot.getParkingType());
		assertTrue(parkingSpot.isAvailable());
	}

	@Test
	public void testGetNextParkingNumberIfAvailableParkingNumberNotFound() throws Exception {
		// GIVEN
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(0);
		// WHEN
		ParkingSpot parkingSpot = parkingService.getNextParkingNumberIfAvailable();
		// THEN
		verify(inputReaderUtil, times(1)).readSelection();
		verify(parkingSpotDAO, times(1)).getNextAvailableSlot(ParkingType.CAR);

		assertNull(parkingSpot);
	}

	@Test
	public void testGetNextParkingNumberIfAvailableParkingNumberWrongArgument() throws Exception {
		// GIVEN
		when(inputReaderUtil.readSelection()).thenReturn(3);
		// WHEN
		ParkingSpot parkingSpot = parkingService.getNextParkingNumberIfAvailable();
		// THEN
		verify(inputReaderUtil, times(1)).readSelection();
		verify(parkingSpotDAO, never()).getNextAvailableSlot(any(ParkingType.class));

		assertNull(parkingSpot);
	}
}