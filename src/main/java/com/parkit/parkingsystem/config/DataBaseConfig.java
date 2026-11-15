package com.parkit.parkingsystem.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * Configuration class for database connections. Provides methods to create and
 * close database connections, prepared statements, and result sets for the
 * parking system.
 * 
 */
public class DataBaseConfig {

	private static final Logger logger = LogManager.getLogger("DataBaseConfig");
	private static final Properties props = new Properties();
	static {
		try (InputStream input = DataBaseConfig.class.getClassLoader().getResourceAsStream("database.properties")) {
			props.load(input);
		} catch (IOException e) {
			logger.error("Error loading database properties", e);
		}
	}

	/**
	 * Creates and returns a new database connection.
	 *
	 * @return a connection to the MySQL database
	 */
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		logger.info("Create DB connection");
		Class.forName("com.mysql.cj.jdbc.Driver");
		return DriverManager.getConnection(props.getProperty("db.url"), props.getProperty("db.user"),
				props.getProperty("db.password"));
	}

	/**
	 * Closes the given database connection.
	 *
	 * @param con the connection to close
	 */
	public void closeConnection(Connection con) {
		if (con != null) {
			try {
				con.close();
				logger.info("Closing DB connection");
			} catch (SQLException e) {
				logger.error("Error while closing connection", e);
			}
		}
	}

	/**
	 * Closes the given prepared statement.
	 *
	 * @param ps the prepared statement to close
	 */
	public void closePreparedStatement(PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
				logger.info("Closing Prepared Statement");
			} catch (SQLException e) {
				logger.error("Error while closing prepared statement", e);
			}
		}
	}

	/**
	 * Closes the given result set.
	 *
	 * @param rs the result set to close
	 */
	public void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
				logger.info("Closing Result Set");
			} catch (SQLException e) {
				logger.error("Error while closing result set", e);
			}
		}
	}
}
