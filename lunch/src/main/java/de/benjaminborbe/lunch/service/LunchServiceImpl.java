package de.benjaminborbe.lunch.service;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
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
import de.benjaminborbe.lunch.wikiconnector.LunchWikiConnector;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.DateUtil;
import de.benjaminborbe.tools.util.Duration;
import de.benjaminborbe.tools.util.DurationUtil;
import de.benjaminborbe.tools.util.ParseException;

@Singleton
public class LunchServiceImpl implements LunchService {

	private static final long DURATION_WARN = 300;

	private final Logger logger;

	private final LunchWikiConnector wikiConnector;

	private final LunchConfig lunchConfig;

	private final AuthenticationService authenticationService;

	private final DateUtil dateUtil;

	private final DurationUtil durationUtil;

	private final CalendarUtil calendarUtil;

	@Inject
	public LunchServiceImpl(
			final Logger logger,
			final LunchWikiConnector wikiConnector,
			final LunchConfig lunchConfig,
			final AuthenticationService authenticationService,
			final DateUtil dateUtil,
			final DurationUtil durationUtil,
			final CalendarUtil calendarUtil) {
		this.logger = logger;
		this.wikiConnector = wikiConnector;
		this.lunchConfig = lunchConfig;
		this.authenticationService = authenticationService;
		this.dateUtil = dateUtil;
		this.durationUtil = durationUtil;
		this.calendarUtil = calendarUtil;
	}

	@Override
	public Collection<Lunch> getLunchs(final SessionIdentifier sessionIdentifier) throws LunchServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("getLunchs for current user");
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			final String username = authenticationService.getFullname(sessionIdentifier, userIdentifier);
			return getLunchs(sessionIdentifier, username);
		}
		catch (final AuthenticationServiceException e) {
			throw new LunchServiceException(e.getClass().getSimpleName(), e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	public Collection<Lunch> getLunchs(final SessionIdentifier sessionIdentifier, final String fullname, final Date date) throws LunchServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("getLunchs - fullname: " + fullname + " date: " + dateUtil.dateString(date));
			final String spaceKey = lunchConfig.getConfluenceSpaceKey();
			final String username = lunchConfig.getConfluenceUsername();
			final String password = lunchConfig.getConfluencePassword();
			return wikiConnector.extractLunchs(spaceKey, username, password, fullname, date);
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
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}

	}

	@Override
	public Collection<Lunch> getLunchs(final SessionIdentifier sessionIdentifier, final String fullname) throws LunchServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("getLunchs - fullname: " + fullname);
			return getLunchs(sessionIdentifier, fullname, dateUtil.today());
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<Lunch> getLunchsArchiv(final SessionIdentifier sessionIdentifier, final String fullname) throws LunchServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("getLunchsArchiv - fullname: " + fullname);
			return getLunchs(sessionIdentifier, fullname, null);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<String> getSubscribeUser(final SessionIdentifier sessionIdentifier, final Calendar day) throws LunchServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("getSubscribeUser for day: " + calendarUtil.toDateString(day));

			final String spaceKey = lunchConfig.getConfluenceSpaceKey();
			final String username = lunchConfig.getConfluenceUsername();
			final String password = lunchConfig.getConfluencePassword();
			return wikiConnector.extractSubscriptions(spaceKey, username, password, day.getTime());
		}
		catch (final AuthenticationFailedException e) {
			throw new LunchServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final RemoteException e) {
			throw new LunchServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final ServiceException e) {
			throw new LunchServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final ParseException e) {
			throw new LunchServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final java.rmi.RemoteException e) {
			throw new LunchServiceException(e.getClass().getSimpleName(), e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}
}
