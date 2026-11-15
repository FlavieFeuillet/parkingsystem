package com.parkit.parkingsystem;

import com.parkit.parkingsystem.service.InteractiveShell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main entry point of the Park'it parking system application. Launches the
 * interactive shell for user interaction.
 * 
 */
public class App {
	private static final Logger logger = LogManager.getLogger("App");

	/**
	 * Main method that starts the parking system.
	 *
	 * @param args command-line arguments (not used)
	 */
	public static void main(String args[]) {
		logger.info("Initializing Parking System");
		InteractiveShell.loadInterface();
	}
}
