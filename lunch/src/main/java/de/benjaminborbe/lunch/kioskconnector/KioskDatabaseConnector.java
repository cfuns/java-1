package de.benjaminborbe.lunch.kioskconnector;

import java.util.Calendar;
import java.util.Collection;

public interface KioskDatabaseConnector {

	KioskUserBean getCustomerNumber(String prename, String surename) throws KioskDatabaseConnectorException;

	Collection<KioskUserBean> getBookingsForDay(Calendar calendar) throws KioskDatabaseConnectorException;
}
