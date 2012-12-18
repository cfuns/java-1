package de.benjaminborbe.lunch.kioskconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
		PreparedStatement statement = null;
		try {
			logger.debug("search customerNumber");
			final String[] parts = username.split(" ", 2);
			if (parts.length == 2) {
				logger.debug("load driver");
				Class.forName("com.mysql.jdbc.Driver").newInstance();

				logger.debug("InitialContext");
				final Context initialContext = new InitialContext();
				final Context componentBindings = (Context) initialContext.lookup("java:comp/env");
				final DataSource dataSource = (DataSource) componentBindings.lookup("jdbc/kiosk");
				logger.debug("getConnection");
				connection = dataSource.getConnection();

				statement = connection.prepareStatement("SELECT customer_no FROM customer WHERE prename = ? AND surname = ?");
				statement.setString(1, parts[0]);
				statement.setString(2, parts[1]);

				final ResultSet r = statement.executeQuery();
				logger.debug("executeQuery");
				while (r.next()) {
					logger.debug("found customerNumber");
					return r.getString(1);
				}
			}
			logger.debug("did not find customerNumber");
			return null;
		}
		catch (final SQLException e) {
			throw new KioskDatabaseConnectorException(e);
		}
		catch (final NamingException e) {
			throw new KioskDatabaseConnectorException(e);
		}
		catch (final InstantiationException e) {
			throw new KioskDatabaseConnectorException(e);
		}
		catch (final IllegalAccessException e) {
			throw new KioskDatabaseConnectorException(e);
		}
		catch (final ClassNotFoundException e) {
			throw new KioskDatabaseConnectorException(e);
		}
		finally {
			if (statement != null) {
				try {
					statement.close();
				}
				catch (final SQLException e) {
				}
			}
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
