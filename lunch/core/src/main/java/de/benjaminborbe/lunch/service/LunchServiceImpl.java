package de.benjaminborbe.lunch.service;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.kiosk.api.KioskService;
import de.benjaminborbe.kiosk.api.KioskServiceException;
import de.benjaminborbe.kiosk.api.KioskUser;
import de.benjaminborbe.kiosk.api.KioskUserDto;
import de.benjaminborbe.lunch.LunchConstants;
import de.benjaminborbe.lunch.api.Lunch;
import de.benjaminborbe.lunch.api.LunchService;
import de.benjaminborbe.lunch.api.LunchServiceException;
import de.benjaminborbe.lunch.config.LunchConfig;
import de.benjaminborbe.lunch.dao.LunchUserSettingsBean;
import de.benjaminborbe.lunch.dao.LunchUserSettingsDao;
import de.benjaminborbe.lunch.wikiconnector.LunchWikiConnector;
import de.benjaminborbe.notification.api.NotificationDto;
import de.benjaminborbe.notification.api.NotificationService;
import de.benjaminborbe.notification.api.NotificationServiceException;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.synchronize.RunOnlyOnceATime;
import de.benjaminborbe.tools.util.Duration;
import de.benjaminborbe.tools.util.DurationUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.validation.ValidationExecutor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.rpc.ServiceException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@Singleton
public class LunchServiceImpl implements LunchService {

	private final class SendBookings implements Runnable {

		private final Collection<Long> users;

		private final Calendar day;

		private SendBookings(final Collection<Long> users, final Calendar day) {
			this.users = users;
			this.day = day;
		}

		@Override
		public void run() {
			try {
				logger.info("book  - day: " + calendarUtil.toDateString(day) + " users: " + StringUtils.join(users, ','));

				for (final Long customer : users) {
					kioskService.book(customer, LunchConstants.MITTAG_EAN);
				}

				sendBookMail(day, users);
			} catch (final KioskServiceException e) {
				logger.warn(e.getClass().getName(), e);
			}
		}
	}

	private static final long DURATION_WARN = 300;

	private final Logger logger;

	private final LunchWikiConnector wikiConnector;

	private final LunchConfig lunchConfig;

	private final AuthenticationService authenticationService;

	private final DurationUtil durationUtil;

	private final CalendarUtil calendarUtil;

	private final AuthorizationService authorizationService;

	private final KioskService kioskService;

	private final LunchUserSettingsDao lunchUserSettingsDao;

	private final ValidationExecutor validationExecutor;

	private final RunOnlyOnceATime runOnlyOnceATime;

	private final NotificationService notificationService;

	@Inject
	public LunchServiceImpl(
		final Logger logger,
		final NotificationService notificationService,
		final RunOnlyOnceATime runOnlyOnceATime,
		final KioskService kioskService,
		final LunchWikiConnector wikiConnector,
		final LunchConfig lunchConfig,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final DurationUtil durationUtil,
		final CalendarUtil calendarUtil,
		final LunchUserSettingsDao lunchUserSettingsDao,
		final ValidationExecutor validationExecutor
	) {
		this.logger = logger;
		this.notificationService = notificationService;
		this.runOnlyOnceATime = runOnlyOnceATime;
		this.kioskService = kioskService;
		this.wikiConnector = wikiConnector;
		this.lunchConfig = lunchConfig;
		this.authenticationService = authenticationService;
		this.authorizationService = authorizationService;
		this.durationUtil = durationUtil;
		this.calendarUtil = calendarUtil;
		this.lunchUserSettingsDao = lunchUserSettingsDao;
		this.validationExecutor = validationExecutor;
	}

	@Override
	public Collection<Lunch> getLunchs(final SessionIdentifier sessionIdentifier) throws LunchServiceException, LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("getLunchs for current user");
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			final String username = authenticationService.getFullname(userIdentifier);
			return getLunchs(sessionIdentifier, username);
		} catch (final AuthenticationServiceException e) {
			throw new LunchServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	private Collection<Lunch> getLunchs(
		final SessionIdentifier sessionIdentifier,
		final String fullname,
		final Calendar date
	) throws LunchServiceException, LoginRequiredException,
		PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			authorizationService.expectPermission(sessionIdentifier, authorizationService.createPermissionIdentifier(PERMISSION_VIEW));

			logger.debug("getLunchs - fullname: " + fullname + " date: " + calendarUtil.toDateString(date));
			final String spaceKey = lunchConfig.getConfluenceSpaceKey();
			final String username = lunchConfig.getConfluenceUsername();
			final String password = lunchConfig.getConfluencePassword();
			return wikiConnector.extractLunchs(spaceKey, username, password, fullname, date);
		} catch (final AuthorizationServiceException | java.rmi.RemoteException | ServiceException | ParseException e) {
			throw new LunchServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}

	}

	@Override
	public Collection<Lunch> getLunchs(final SessionIdentifier sessionIdentifier, final String fullname) throws LunchServiceException, LoginRequiredException,
		PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			authorizationService.expectPermission(sessionIdentifier, authorizationService.createPermissionIdentifier(PERMISSION_VIEW));

			logger.debug("getLunchs - fullname: " + fullname);
			return getLunchs(sessionIdentifier, fullname, calendarUtil.today());
		} catch (final AuthorizationServiceException e) {
			throw new LunchServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<Lunch> getLunchsArchiv(
		final SessionIdentifier sessionIdentifier,
		final String fullname
	) throws LunchServiceException, LoginRequiredException,
		PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("getLunchsArchiv - fullname: " + fullname);
			return getLunchs(sessionIdentifier, fullname, null);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<KioskUser> getSubscribeUser(final Calendar day) throws LunchServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("getSubscribeUser for day: " + calendarUtil.toDateString(day));
			final String confluenceSpaceKey = lunchConfig.getConfluenceSpaceKey();
			final String confluenceUsername = lunchConfig.getConfluenceUsername();
			final String confluencePassword = lunchConfig.getConfluencePassword();
			final Collection<String> list = wikiConnector.extractSubscriptions(confluenceSpaceKey, confluenceUsername, confluencePassword, day);

			final List<KioskUser> result = new ArrayList<>();
			for (final String username : list) {
				result.add(buildUser(username));
			}
			return result;
		} catch (final KioskServiceException | ParseException | ServiceException | java.rmi.RemoteException e) {
			throw new LunchServiceException(e);
		} finally {
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
		} else {
			final KioskUserDto user = new KioskUserDto();
			user.setPrename(username);
			return user;
		}
	}

	@Override
	public void book(
		final SessionIdentifier sessionIdentifier,
		final Calendar day,
		final Collection<Long> users
	) throws LunchServiceException, LoginRequiredException,
		PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			final PermissionIdentifier roleIdentifier = authorizationService.createPermissionIdentifier(PERMISSION_BOOKING);
			authorizationService.expectPermission(sessionIdentifier, roleIdentifier);
			runOnlyOnceATime.run(new SendBookings(users, day));
		} catch (final AuthorizationServiceException e) {
			throw new LunchServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	private void sendBookMail(final Calendar day, final Collection<Long> users) {
		try {
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
			final String message = sb.toString();
			final UserIdentifier userIdentifier = authenticationService.createUserIdentifier("bborbe");

			final NotificationDto notification = new NotificationDto();
			notification.setTo(userIdentifier);
			notification.setType(notificationService.createNotificationTypeIdentifier("lunchBooking"));
			notification.setSubject(subject);
			notification.setMessage(message);
			notificationService.notify(notification);
		} catch (final NotificationServiceException | ValidationException | AuthenticationServiceException e) {
			logger.warn("send book mail failed", e);
		}
	}

	@Override
	public Collection<KioskUser> getBookedUser(final Calendar day) throws LunchServiceException, LoginRequiredException,
		PermissionDeniedException {
		try {
			final List<KioskUser> result = new ArrayList<>();
			for (final KioskUser user : kioskService.getBookingsForDay(day, LunchConstants.MITTAG_EAN)) {
				result.add(user);
			}
			return result;
		} catch (final KioskServiceException e) {
			throw new LunchServiceException(e);
		}
	}

	@Override
	public boolean isNotificationActivated(final UserIdentifier userIdentifier) throws LunchServiceException {
		try {
			final LunchUserSettingsBean bean = lunchUserSettingsDao.findOrCreate(userIdentifier);
			final Boolean value = bean.getNotificationActivated();
			logger.debug("activ = " + value);
			return Boolean.TRUE.equals(value);
		} catch (final StorageException e) {
			throw new LunchServiceException(e);
		}
	}

	@Override
	public void activateNotification(final UserIdentifier userIdentifier) throws LunchServiceException, ValidationException {
		try {
			final LunchUserSettingsBean bean = lunchUserSettingsDao.findOrCreate(userIdentifier);
			bean.setNotificationActivated(true);

			final ValidationResult errors = validationExecutor.validate(bean);
			if (errors.hasErrors()) {
				logger.warn(bean.getClass().getSimpleName() + " " + errors.toString());
				throw new ValidationException(errors);
			}

			lunchUserSettingsDao.save(bean);
		} catch (final StorageException e) {
			throw new LunchServiceException(e);
		}
	}

	@Override
	public void deactivateNotification(final UserIdentifier userIdentifier) throws LunchServiceException, ValidationException {
		try {
			final LunchUserSettingsBean bean = lunchUserSettingsDao.findOrCreate(userIdentifier);
			bean.setNotificationActivated(false);

			final ValidationResult errors = validationExecutor.validate(bean);
			if (errors.hasErrors()) {
				logger.warn("TaskContext " + errors.toString());
				throw new ValidationException(errors);
			}
			lunchUserSettingsDao.save(bean);
		} catch (final StorageException e) {
			throw new LunchServiceException(e);
		}
	}
}
