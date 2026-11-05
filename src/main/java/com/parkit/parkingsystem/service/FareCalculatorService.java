package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		// Entry and exit time of vehicle in milliseconds
		long inMillis = ticket.getInTime().getTime();
		long outMillis = ticket.getOutTime().getTime();

		// TODO: Some tests are failing here: use milliseconds and convert to minutes
		// then to hours
		long durationInMillis = outMillis - inMillis;
		double durationInMinutes = durationInMillis / (1000.0 * 60);
		double durationInHours = durationInMinutes / 60.0;

		// For less than 30 minutes: free parking
		if (durationInMinutes < 30) {
			ticket.setPrice(0.0);
			return;
		}
		// Normal fare
		switch (ticket.getParkingSpot().getParkingType()) {
		case CAR: {
			ticket.setPrice(durationInHours * Fare.CAR_RATE_PER_HOUR);
			break;
		}
		case BIKE: {
			ticket.setPrice(durationInHours * Fare.BIKE_RATE_PER_HOUR);
			break;
		}
		default:
			throw new IllegalArgumentException("Unkown Parking Type");
		}
	}
}