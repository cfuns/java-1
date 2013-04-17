package de.benjaminborbe.lunch.wikiconnector;

import de.benjaminborbe.lunch.api.Lunch;
import de.benjaminborbe.tools.util.ParseException;

import javax.xml.rpc.ServiceException;
import java.util.Calendar;
import java.util.Collection;

public interface LunchWikiConnector {

	Collection<Lunch> extractLunchs(String spaceKey, String username, String password, String fullname, Calendar date) throws ServiceException,
		java.rmi.RemoteException, ParseException;

	Collection<String> extractSubscriptions(final String spaceKey, final String username, final String password, final Calendar date) throws ServiceException,
		java.rmi.RemoteException, ParseException;

}
