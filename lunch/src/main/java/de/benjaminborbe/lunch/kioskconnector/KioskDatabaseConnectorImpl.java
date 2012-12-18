package de.benjaminborbe.lunch.kioskconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.slf4j.Logger;

import com.google.inject.Inject;

public class KioskDatabaseConnectorImpl implements KioskDatabaseConnector {

	private final Logger logger;

	@Inject
	public KioskDatabaseConnectorImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public String getCustomerNumber(final String username) throws KioskDatabaseConnectorException {
		Connection connection = null;
		try {
			logger.debug("search customerNumber");
			final Context initialContext = new InitialContext();
			final Context componentBindings = (Context) initialContext.lookup("java:comp/env");
			final DataSource dataSource = (DataSource) componentBindings.lookup("jdbc/kiosk");
			connection = dataSource.getConnection();

			final String[] parts = username.split(" ", 2);

			final PreparedStatement s = connection.prepareStatement("SELECT customer_no FROM customer WHERE prename = ? AND surname = ?");
			s.setString(1, parts[0]);
			s.setString(2, parts[1]);

			final ResultSet r = s.executeQuery();
			while (r.next()) {
				return r.getString(1);
			}
			return null;
		}
		catch (final Exception e) {
			throw new KioskDatabaseConnectorException(e);
		}
		finally {
			if (connection != null) {
				try {
					connection.close();
				}
				catch (final SQLException e) {
				}
			}
		}
	}
}
