package de.benjaminborbe.lunch.wikiconnector;

import java.util.Collection;
import java.util.Date;

import javax.xml.rpc.ServiceException;

import com.atlassian.confluence.rpc.AuthenticationFailedException;
import com.atlassian.confluence.rpc.RemoteException;

import de.benjaminborbe.lunch.api.Lunch;
import de.benjaminborbe.tools.util.ParseException;

public interface LunchWikiConnector {

	Collection<Lunch> extractLunchs(String spaceKey, String username, String password, String fullname, Date date) throws ServiceException, AuthenticationFailedException,
			RemoteException, java.rmi.RemoteException, ParseException;
}
