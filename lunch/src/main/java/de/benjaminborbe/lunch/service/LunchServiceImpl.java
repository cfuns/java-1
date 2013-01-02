package de.benjaminborbe.lunch.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

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
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.authorization.api.RoleIdentifier;
import de.benjaminborbe.lunch.LunchConstants;
import de.benjaminborbe.lunch.api.Lunch;
import de.benjaminborbe.lunch.api.LunchService;
import de.benjaminborbe.lunch.api.LunchServiceException;
import de.benjaminborbe.lunch.api.LunchUser;
import de.benjaminborbe.lunch.booking.LunchBookingMessage;
import de.benjaminborbe.lunch.booking.LunchBookingMessageMapper;
import de.benjaminborbe.lunch.config.LunchConfig;
import de.benjaminborbe.lunch.kioskconnector.KioskDatabaseConnector;
import de.benjaminborbe.lunch.kioskconnector.KioskDatabaseConnectorException;
import de.benjaminborbe.lunch.wikiconnector.LunchWikiConnector;
import de.benjaminborbe.mail.api.MailDto;
import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.mail.api.MailServiceException;
import de.benjaminborbe.message.api.MessageService;
import de.benjaminborbe.message.api.MessageServiceException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.util.Duration;
import de.benjaminborbe.tools.util.DurationUtil;
import de.benjaminborbe.tools.util.ParseException;

@Singleton
public class LunchServiceImpl implements LunchService {

	private final class LunchUserImpl implements LunchUser {

		private final String username;

		private final String customerNumber;

		private LunchUserImpl(final String username, final String customerNumber) {
			this.username = username;
			this.customerNumber = customerNumber;
		}

		@Override
		public String getName() {
			return username;
		}

		@Override
		public String getCustomerNumber() {
			return customerNumber;
		}
	}

	private static final long DURATION_WARN = 300;

	private final Logger logger;

	private final LunchWikiConnector wikiConnector;

	private final LunchConfig lunchConfig;

	private final AuthenticationService authenticationService;

	private final DurationUtil durationUtil;

	private final CalendarUtil calendarUtil;

	private final MessageService messageService;

	private final LunchBookingMessageMapper bookingMessageMapper;

	private final KioskDatabaseConnector kioskDatabaseConnector;

	private final AuthorizationService authorizationService;

	private final MailService mailService;

	@Inject
	public LunchServiceImpl(
			final Logger logger,
			final KioskDatabaseConnector kioskDatabaseConnector,
			final LunchBookingMessageMapper bookingMessageMapper,
			final MessageService messageService,
			final LunchWikiConnector wikiConnector,
			final LunchConfig lunchConfig,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final DurationUtil durationUtil,
			final CalendarUtil calendarUtil,
			final MailService mailService) {
		this.logger = logger;
		this.kioskDatabaseConnector = kioskDatabaseConnector;
		this.bookingMessageMapper = bookingMessageMapper;
		this.messageService = messageService;
		this.wikiConnector = wikiConnector;
		this.lunchConfig = lunchConfig;
		this.authenticationService = authenticationService;
		this.authorizationService = authorizationService;
		this.durationUtil = durationUtil;
		this.calendarUtil = calendarUtil;
		this.mailService = mailService;
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
	public Collection<LunchUser> getSubscribeUser(final SessionIdentifier sessionIdentifier, final Calendar day) throws LunchServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("getSubscribeUser for day: " + calendarUtil.toDateString(day));
			final String confluenceSpaceKey = lunchConfig.getConfluenceSpaceKey();
			final String confluenceUsername = lunchConfig.getConfluenceUsername();
			final String confluencePassword = lunchConfig.getConfluencePassword();
			final Collection<String> list = wikiConnector.extractSubscriptions(confluenceSpaceKey, confluenceUsername, confluencePassword, day);

			final List<LunchUser> result = new ArrayList<LunchUser>();
			for (final String username : list) {
				try {
					final String customerNumber = kioskDatabaseConnector.getCustomerNumber(username);
					result.add(new LunchUserImpl(username, customerNumber));
				}
				catch (final KioskDatabaseConnectorException e) {
					logger.warn(e.getClass().getName(), e);
				}
			}
			return result;
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
	public void book(final SessionIdentifier sessionIdentifier, final Calendar day, final Collection<String> users) throws LunchServiceException, LoginRequiredException,
			PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			final RoleIdentifier roleIdentifier = authorizationService.createRoleIdentifier(LunchConstants.LUNCH_ADMIN_ROLENAME);
			authorizationService.expectRole(sessionIdentifier, roleIdentifier);
			logger.debug("book");

			for (final String user : users) {
				logger.debug("send booking message for " + user + " " + calendarUtil.toDateString(day));
				final LunchBookingMessage bookingMessage = new LunchBookingMessage(user, day);
				messageService.sendMessage(LunchConstants.BOOKING_MESSAGE_TYPE, bookingMessageMapper.map(bookingMessage));
			}

			sendBookMail(day, users);
		}
		catch (final MessageServiceException e) {
			throw new LunchServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final MapException e) {
			throw new LunchServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final AuthorizationServiceException e) {
			throw new LunchServiceException(e.getClass().getSimpleName(), e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	private void sendBookMail(final Calendar day, final Collection<String> users) {
		try {
			final String from = "bborbe@seibert-media.net";
			final String to = "bborbe@seibert-media.net";
			final String subject = "Mittag - Book";
			final StringBuilder sb = new StringBuilder();
			sb.append("day: ");
			sb.append(calendarUtil.toDateString(day));
			sb.append("\n");
			sb.append("\n");
			sb.append("users:\n");
			for (final String user : users) {
				sb.append("- ");
				sb.append(user);
				sb.append("\n");
			}
			final String contentType = "text/plain";
			final MailDto mail = new MailDto(from, to, subject, sb.toString(), contentType);
			mailService.send(mail);
		}
		catch (final MailServiceException e) {
			logger.warn("send book mail failed", e);
		}
	}
}
