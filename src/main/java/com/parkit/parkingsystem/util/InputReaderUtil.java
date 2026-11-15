package com.parkit.parkingsystem.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

/**
 * Utility class for reading user input from the console. Provides methods to
 * read menu selections and vehicle registration numbers, with basic validation
 * and error handling.
 *
 */
public class InputReaderUtil {

	private static Scanner scan = new Scanner(System.in, "UTF-8");
	private static final Logger logger = LogManager.getLogger("InputReaderUtil");

	/**
	 * Reads a menu selection from the user.
	 *
	 * @return the selected option as an integer or -1 if the input is invalid
	 */
	public int readSelection() {
		try {
			int input = Integer.parseInt(scan.nextLine());
			return input;
		} catch (Exception e) {
			logger.error("Error while reading user input from Shell", e);
			System.out.println("Error reading input. Please enter valid number for proceeding further");
			return -1;
		}
	}

	/**
	 * Reads a vehicle registration number from the user.
	 *
	 * @return the vehicle registration number
	 */
	public String readVehicleRegistrationNumber() throws Exception {
		try {
			String vehicleRegNumber = scan.nextLine();
			if (vehicleRegNumber == null || vehicleRegNumber.trim().length() == 0) {
				throw new IllegalArgumentException("Invalid input provided");
			}
			return vehicleRegNumber;
		} catch (Exception e) {
			logger.error("Error while reading user input from Shell", e);
			System.out.println("Error reading input. Please enter a valid string for vehicle registration number");
			throw e;
		}
	}
}
