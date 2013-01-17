package de.benjaminborbe.lunch.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.apache.commons.lang.StringUtils;
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
import de.benjaminborbe.kiosk.api.KioskService;
import de.benjaminborbe.kiosk.api.KioskServiceException;
import de.benjaminborbe.kiosk.api.KioskUser;
import de.benjaminborbe.kiosk.api.KioskUserDto;
import de.benjaminborbe.lunch.LunchConstants;
import de.benjaminborbe.lunch.api.Lunch;
import de.benjaminborbe.lunch.api.LunchService;
import de.benjaminborbe.lunch.api.LunchServiceException;
import de.benjaminborbe.lunch.config.LunchConfig;
import de.benjaminborbe.lunch.wikiconnector.LunchWikiConnector;
import de.benjaminborbe.mail.api.MailDto;
import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.mail.api.MailServiceException;
import de.benjaminborbe.tools.date.CalendarUtil;
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

	private final AuthorizationService authorizationService;

	private final MailService mailService;

	private final KioskService kioskService;

	@Inject
	public LunchServiceImpl(
			final Logger logger,
			final KioskService kioskService,
			final LunchWikiConnector wikiConnector,
			final LunchConfig lunchConfig,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final DurationUtil durationUtil,
			final CalendarUtil calendarUtil,
			final MailService mailService) {
		this.logger = logger;
		this.kioskService = kioskService;
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
	public Collection<KioskUser> getSubscribeUser(final SessionIdentifier sessionIdentifier, final Calendar day) throws LunchServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("getSubscribeUser for day: " + calendarUtil.toDateString(day));
			final String confluenceSpaceKey = lunchConfig.getConfluenceSpaceKey();
			final String confluenceUsername = lunchConfig.getConfluenceUsername();
			final String confluencePassword = lunchConfig.getConfluencePassword();
			final Collection<String> list = wikiConnector.extractSubscriptions(confluenceSpaceKey, confluenceUsername, confluencePassword, day);

			final List<KioskUser> result = new ArrayList<KioskUser>();
			for (final String username : list) {
				result.add(buildUser(username));
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
		catch (final KioskServiceException e) {
			throw new LunchServiceException(e.getClass().getSimpleName(), e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	private KioskUser buildUser(final String username) throws KioskServiceException {
		final String[] parts = username.split(" ", 2);
		if (parts.length == 2) {
			{
				final KioskUser user = kioskService.getCustomerNumber(parts[0], parts[1]);
				if (user != null) {
					return user;
				}
			}
			{
				final KioskUserDto user = new KioskUserDto();
				user.setPrename(parts[0]);
				user.setSurname(parts[1]);
				return user;
			}
		}
		else {
			final KioskUserDto user = new KioskUserDto();
			user.setPrename(username);
			return user;
		}
	}

	@Override
	public void book(final SessionIdentifier sessionIdentifier, final Calendar day, final Collection<Long> users) throws LunchServiceException, LoginRequiredException,
			PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			final RoleIdentifier roleIdentifier = authorizationService.createRoleIdentifier(LUNCH_ADMIN_ROLENAME);
			authorizationService.expectRole(sessionIdentifier, roleIdentifier);
			logger.info("book  - day: " + calendarUtil.toDateString(day) + " users: " + StringUtils.join(users, ','));

			for (final Long customer : users) {
				kioskService.book(customer, LunchConstants.MITTAG_EAN);
			}

			sendBookMail(day, users);
		}
		catch (final AuthorizationServiceException e) {
			throw new LunchServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final KioskServiceException e) {
			throw new LunchServiceException(e.getClass().getSimpleName(), e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	private void sendBookMail(final Calendar day, final Collection<Long> users) {
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
			for (final Long user : users) {
				sb.append("- ");
				sb.append(String.valueOf(user));
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

	@Override
	public Collection<KioskUser> getBookedUser(final SessionIdentifier sessionIdentifier, final Calendar day) throws LunchServiceException, LoginRequiredException,
			PermissionDeniedException {
		try {
			final List<KioskUser> result = new ArrayList<KioskUser>();
			for (final KioskUser user : kioskService.getBookingsForDay(day, LunchConstants.MITTAG_EAN)) {
				result.add(user);
			}
			return result;
		}
		catch (final KioskServiceException e) {
			throw new LunchServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public boolean isNotificationActivated(final UserIdentifier userIdentifier) throws LunchServiceException {
		return false;
	}

	@Override
	public void activateNotification(final UserIdentifier userIdentifier) throws LunchServiceException {
	}

	@Override
	public void deactivateNotification(final UserIdentifier userIdentifier) throws LunchServiceException {
	}
}
