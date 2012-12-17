package de.benjaminborbe.lunch.service;

import java.util.Calendar;
import java.util.Collection;
import javax.xml.rpc.ServiceException;

import org.slf4j.Logger;

import com.atlassian.confluence.rpc.AuthenticationFailedException;
import com.atlassian.confluence.rpc.RemoteException;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.lunch.LunchConstants;
import de.benjaminborbe.lunch.api.Lunch;
import de.benjaminborbe.lunch.api.LunchService;
import de.benjaminborbe.lunch.api.LunchServiceException;
import de.benjaminborbe.lunch.booking.BookingMessage;
import de.benjaminborbe.lunch.booking.BookingMessageMapper;
import de.benjaminborbe.lunch.config.LunchConfig;
import de.benjaminborbe.lunch.wikiconnector.LunchWikiConnector;
import de.benjaminborbe.messageservice.api.MessageService;
import de.benjaminborbe.messageservice.api.MessageServiceException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.mapper.MapException;
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

	private final DurationUtil durationUtil;

	private final CalendarUtil calendarUtil;

	private final MessageService messageService;

	private final BookingMessageMapper bookingMessageMapper;

	@Inject
	public LunchServiceImpl(
			final Logger logger,
			final BookingMessageMapper bookingMessageMapper,
			final MessageService messageService,
			final LunchWikiConnector wikiConnector,
			final LunchConfig lunchConfig,
			final AuthenticationService authenticationService,
			final DurationUtil durationUtil,
			final CalendarUtil calendarUtil) {
		this.logger = logger;
		this.bookingMessageMapper = bookingMessageMapper;
		this.messageService = messageService;
		this.wikiConnector = wikiConnector;
		this.lunchConfig = lunchConfig;
		this.authenticationService = authenticationService;
		this.durationUtil = durationUtil;
		this.calendarUtil = calendarUtil;
	}

	@Override
	public Collection<Lunch> getLunchs(final SessionIdentifier sessionIdentifier) throws LunchServiceException, LoginRequiredException {
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

	private Collection<Lunch> getLunchs(final SessionIdentifier sessionIdentifier, final String fullname, final Calendar date) throws LunchServiceException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);

			logger.debug("getLunchs - fullname: " + fullname + " date: " + calendarUtil.toDateString(date));
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
		catch (final AuthenticationServiceException e) {
			throw new LunchServiceException(e.getClass().getSimpleName(), e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}

	}

	@Override
	public Collection<Lunch> getLunchs(final SessionIdentifier sessionIdentifier, final String fullname) throws LunchServiceException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);

			logger.debug("getLunchs - fullname: " + fullname);
			return getLunchs(sessionIdentifier, fullname, calendarUtil.today());
		}
		catch (final AuthenticationServiceException e) {
			throw new LunchServiceException(e.getClass().getSimpleName(), e);
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
		catch (final LoginRequiredException e) {
			throw new LunchServiceException(e.getClass().getSimpleName(), e);
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
			return wikiConnector.extractSubscriptions(spaceKey, username, password, day);
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

	@Override
	public void book(final SessionIdentifier sessionIdentifier, final Calendar day, final Collection<String> users) throws LunchServiceException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);
			logger.debug("book");

			for (final String user : users) {
				final BookingMessage bookingMessage = new BookingMessage(user, day);
				messageService.sendMessage(LunchConstants.BOOKING_MESSAGE_TYPE, bookingMessageMapper.map(bookingMessage));
			}
		}
		catch (final MessageServiceException e) {
			throw new LunchServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final AuthenticationServiceException e) {
			throw new LunchServiceException(e.getClass().getSimpleName(), e);
		}
		catch (MapException e) {
			throw new LunchServiceException(e.getClass().getSimpleName(), e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}
}
