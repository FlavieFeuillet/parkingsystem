package com.parkit.parkingsystem.model;

import java.util.Date;

/**
 * Represents a parking ticket. Contains information about the vehicle, parking
 * spot, entry/exit times, and the calculated price.
 * 
 */
public class Ticket {
	private int id;
	private ParkingSpot parkingSpot;
	private String vehicleRegNumber;
	private double price;
	private Date inTime;
	private Date outTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getVehicleRegNumber() {
		return vehicleRegNumber;
	}

	public void setVehicleRegNumber(String vehicleRegNumber) {
		this.vehicleRegNumber = vehicleRegNumber;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public ParkingSpot getParkingSpot() {
		if (parkingSpot == null) {
			return null;
		}
		return new ParkingSpot(parkingSpot.getId(), parkingSpot.getParkingType(), parkingSpot.isAvailable());
	}

	public void setParkingSpot(ParkingSpot parkingSpot) {
		if (parkingSpot == null) {
			this.parkingSpot = null;
		} else {
			this.parkingSpot = new ParkingSpot(parkingSpot.getId(), parkingSpot.getParkingType(),
					parkingSpot.isAvailable());
		}
	}

	public Date getInTime() {
		return inTime == null ? null : new Date(inTime.getTime());
	}

	public void setInTime(Date inTime) {
		this.inTime = inTime == null ? null : new Date(inTime.getTime());
	}

	public Date getOutTime() {
		return outTime == null ? null : new Date(outTime.getTime());
	}

	public void setOutTime(Date outTime) {
		this.outTime = outTime == null ? null : new Date(outTime.getTime());
	}
}
