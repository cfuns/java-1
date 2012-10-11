package de.benjaminborbe.lunch.service;

import java.util.Collection;

import javax.xml.rpc.ServiceException;

import org.slf4j.Logger;

import com.atlassian.confluence.rpc.AuthenticationFailedException;
import com.atlassian.confluence.rpc.RemoteException;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.lunch.api.Lunch;
import de.benjaminborbe.lunch.api.LunchService;
import de.benjaminborbe.lunch.api.LunchServiceException;
import de.benjaminborbe.lunch.config.LunchConfig;
import de.benjaminborbe.lunch.connector.WikiConnector;
import de.benjaminborbe.tools.util.ParseException;

@Singleton
public class LunchServiceImpl implements LunchService {

	private final Logger logger;

	private final WikiConnector wikiConnector;

	private final LunchConfig lunchConfig;

	private final AuthenticationService authenticationService;

	@Inject
	public LunchServiceImpl(final Logger logger, final WikiConnector wikiConnector, final LunchConfig lunchConfig, final AuthenticationService authenticationService) {
		this.logger = logger;
		this.wikiConnector = wikiConnector;
		this.lunchConfig = lunchConfig;
		this.authenticationService = authenticationService;
	}

	@Override
	public Collection<Lunch> getLunchs(final SessionIdentifier sessionIdentifier) throws LunchServiceException {
		try {
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			final String username = authenticationService.getFullname(sessionIdentifier, userIdentifier);
			return getLunchs(sessionIdentifier, username);
		}
		catch (final AuthenticationServiceException e) {
			throw new LunchServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public Collection<Lunch> getLunchs(final SessionIdentifier sessionIdentifier, final String fullname) throws LunchServiceException {
		logger.trace("getLunchs");
		try {
			final String spaceKey = lunchConfig.getConfluenceSpaceKey();
			final String username = lunchConfig.getConfluenceUsername();
			final String password = lunchConfig.getConfluencePassword();
			return wikiConnector.extractLunchs(spaceKey, username, password, fullname);
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
