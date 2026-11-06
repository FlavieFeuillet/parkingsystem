package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

/**
 * Service responsible for calculating parking fares. Computes the price based
 * on parking duration and vehicle type. Free parking for stays under 30 minutes
 * and 5% discount for regular parking users.
 */
public class FareCalculatorService {

	/**
	 * Calculates the parking fare for a given ticket. Computes the price based on
	 * parking duration and vehicle type (hourly rate depends on vehicle type (car
	 * or bike). Free if duration is less than 30 minutes and 5% discount applied
	 * for regular parking users.
	 *
	 * @param ticket   the ticket containing parking informations
	 * @param discount 5% discount should be applied for recurring parking users
	 */
	public void calculateFare(Ticket ticket, boolean discount) {
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
		double price = 0;
		switch (ticket.getParkingSpot().getParkingType()) {
		case CAR: {
			price = durationInHours * Fare.CAR_RATE_PER_HOUR;
			break;
		}
		case BIKE: {
			price = durationInHours * Fare.BIKE_RATE_PER_HOUR;
			break;
		}
		default:
			throw new IllegalArgumentException("Unkown Parking Type");
		}

		// For regular parking users: 5% discount
		if (discount) {
			ticket.setPrice(0.95 * price);
		} else {
			ticket.setPrice(price);
		}
	}

	/**
	 * Calculates the parking fare without discount.
	 *
	 * @param ticket the ticket containing parking information
	 */
	public void calculateFare(Ticket ticket) {
		calculateFare(ticket, false);
	}
}