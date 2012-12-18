package de.benjaminborbe.lunch.kioskconnector;

public interface KioskDatabaseConnector {

	String getCustomerNumber(String username) throws KioskDatabaseConnectorException;

}
