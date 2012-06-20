package de.benjaminborbe.lunch.service;

import java.util.Collection;

import javax.naming.NamingException;
import javax.xml.rpc.ServiceException;

import org.slf4j.Logger;

import com.atlassian.confluence.rpc.AuthenticationFailedException;
import com.atlassian.confluence.rpc.RemoteException;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.lunch.api.Lunch;
import de.benjaminborbe.lunch.api.LunchService;
import de.benjaminborbe.lunch.api.LunchServiceException;
import de.benjaminborbe.lunch.connector.WikiConnector;
import de.benjaminborbe.tools.jndi.JndiContext;
import de.benjaminborbe.tools.util.ParseException;

@Singleton
public class LunchServiceImpl implements LunchService {

	private final Logger logger;

	private final WikiConnector wikiConnector;

	private final String spaceKey = "MITTAG";

	private final JndiContext jndiContext;

	@Inject
	public LunchServiceImpl(final Logger logger, final WikiConnector wikiConnector, final JndiContext jndiContext) {
		this.logger = logger;
		this.wikiConnector = wikiConnector;
		this.jndiContext = jndiContext;
	}

	@Override
	public Collection<Lunch> getLunchs(final SessionIdentifier sessionIdentifier) throws LunchServiceException {
		logger.debug("getLunchs");

		try {
			final String username = (String) jndiContext.lookup("mittag_username");
			final String password = (String) jndiContext.lookup("mittag_password");
			return wikiConnector.extractLunchs(spaceKey, username, password);
		}
		catch (final NamingException e) {
			throw new LunchServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final AuthenticationFailedException e) {
			throw new LunchServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final RemoteException e) {
			throw new LunchServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final java.rmi.RemoteException e) {
			throw new LunchServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final ServiceException e) {
			throw new LunchServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final ParseException e) {
			throw new LunchServiceException(e.getClass().getSimpleName(), e);
		}
	}

}
