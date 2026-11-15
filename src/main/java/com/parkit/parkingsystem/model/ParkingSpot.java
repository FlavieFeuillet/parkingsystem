package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

/**
 * Represents a parking spot in the parking system. A parking spot has a unique
 * number, a vehicle type (car or bike), and an availability status.
 * 
 */
public class ParkingSpot {
	private int number;
	private ParkingType parkingType;
	private boolean isAvailable;

	/**
	 * Creates a new parking spot.
	 *
	 * @param number      the unique identifier of the parking spot
	 * @param parkingType the type of vehicle allowed (CAR or BIKE)
	 * @param isAvailable true if the spot is available
	 */
	public ParkingSpot(int number, ParkingType parkingType, boolean isAvailable) {
		this.number = number;
		this.parkingType = parkingType;
		this.isAvailable = isAvailable;
	}

	public int getId() {
		return number;
	}

	public void setId(int number) {
		this.number = number;
	}

	public ParkingType getParkingType() {
		return parkingType;
	}

	public void setParkingType(ParkingType parkingType) {
		this.parkingType = parkingType;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean available) {
		isAvailable = available;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ParkingSpot that = (ParkingSpot) o;
		return number == that.number;
	}

	@Override
	public int hashCode() {
		return number;
	}
}
