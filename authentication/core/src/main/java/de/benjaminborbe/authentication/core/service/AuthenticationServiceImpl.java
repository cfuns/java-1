package de.benjaminborbe.authentication.core.service;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.SuperAdminRequiredException;
import de.benjaminborbe.authentication.api.User;
import de.benjaminborbe.authentication.api.UserDto;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authentication.core.AuthenticationConstants;
import de.benjaminborbe.authentication.core.config.AuthenticationConfig;
import de.benjaminborbe.authentication.core.dao.SessionBean;
import de.benjaminborbe.authentication.core.dao.SessionDao;
import de.benjaminborbe.authentication.core.dao.UserBean;
import de.benjaminborbe.authentication.core.dao.UserBeanMapper;
import de.benjaminborbe.authentication.core.dao.UserDao;
import de.benjaminborbe.authentication.core.util.AuthenticationGeneratePasswordFailedException;
import de.benjaminborbe.authentication.core.util.AuthenticationPasswordEncryptionService;
import de.benjaminborbe.authentication.core.util.AuthenticationPasswordUtil;
import de.benjaminborbe.authentication.core.verifycredential.AuthenticationVerifyCredential;
import de.benjaminborbe.lib.validation.ValidationExecutor;
import de.benjaminborbe.lib.validation.ValidationResultImpl;
import de.benjaminborbe.mail.api.MailDto;
import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.mail.api.MailServiceException;
import de.benjaminborbe.shortener.api.ShortenerService;
import de.benjaminborbe.shortener.api.ShortenerServiceException;
import de.benjaminborbe.shortener.api.ShortenerUrlIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.storage.tools.IdentifierIteratorException;
import de.benjaminborbe.storage.tools.StorageValueList;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.util.Duration;
import de.benjaminborbe.tools.util.DurationUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

@Singleton
public class AuthenticationServiceImpl implements AuthenticationService {

	private static final int DURATION_WARN = 300;

	private final Logger logger;

	private final SessionDao sessionDao;

	private final UserDao userDao;

	private final AuthenticationPasswordEncryptionService passwordEncryptionService;

	private final TimeZoneUtil timeZoneUtil;

	private final ValidationExecutor validationExecutor;

	private final MailService mailService;

	private final ShortenerService shortenerService;

	private final ParseUtil parseUtil;

	private final DurationUtil durationUtil;

	private final AuthenticationVerifyCredential authenticationVerifyCredential;

	private final AuthenticationPasswordUtil authenticationPasswordUtil;

	private final CalendarUtil calendarUtil;

	private final AuthenticationConfig authenticationConfig;

	@Inject
	public AuthenticationServiceImpl(
		final Logger logger,
		final AuthenticationConfig authenticationConfig,
		final CalendarUtil calendarUtil,
		final AuthenticationPasswordUtil authenticationPasswordUtil,
		final ParseUtil parseUtil,
		final MailService mailService,
		final SessionDao sessionDao,
		final UserDao userDao,
		final AuthenticationPasswordEncryptionService passwordEncryptionService,
		final TimeZoneUtil timeZoneUtil,
		final ValidationExecutor validationExecutor,
		final ShortenerService shortenerService,
		final DurationUtil durationUtil,
		final AuthenticationVerifyCredential authenticationVerifyCredential
	) {
		this.logger = logger;
		this.authenticationConfig = authenticationConfig;
		this.calendarUtil = calendarUtil;
		this.authenticationPasswordUtil = authenticationPasswordUtil;
		this.parseUtil = parseUtil;
		this.mailService = mailService;
		this.sessionDao = sessionDao;
		this.userDao = userDao;
		this.passwordEncryptionService = passwordEncryptionService;
		this.timeZoneUtil = timeZoneUtil;
		this.validationExecutor = validationExecutor;
		this.shortenerService = shortenerService;
		this.durationUtil = durationUtil;
		this.authenticationVerifyCredential = authenticationVerifyCredential;
	}

	@Override
	public boolean verifyCredential(final UserIdentifier userIdentifier, final String password) throws AuthenticationServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			try {
				// delay login
				Thread.sleep(AuthenticationConstants.LOGIN_DELAY);
			} catch (final InterruptedException e) {
			}

			return authenticationVerifyCredential.verifyCredential(userIdentifier, password);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public boolean login(
		final SessionIdentifier sessionIdentifier,
		final UserIdentifier userIdentifier,
		final String password
	) throws AuthenticationServiceException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("try login user: " + userIdentifier);
			if (verifyCredential(userIdentifier, password)) {
				final SessionBean session = sessionDao.findOrCreate(sessionIdentifier);
				session.setCurrentUser(userIdentifier);
				sessionDao.save(session);

				final UserBean user = userDao.load(userIdentifier);
				if (user != null) {
					user.increaseLoginCounter();
					user.setLoginDate(calendarUtil.now());
					userDao.save(user, new StorageValueList(userDao.getEncoding()).add(UserBeanMapper.LOGIN_COUNTER).add(UserBeanMapper.LOGIN_DATE));
					logger.info("local user " + userIdentifier + " logged in successful");
					return true;
				} else {
					logger.info("remote user " + userIdentifier + " logged in successful");
					return true;
				}
			} else {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("login failed")));
			}
		} catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public boolean isLoggedIn(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			final SessionBean session = sessionDao.load(sessionIdentifier);
			return session != null && session.getCurrentUser() != null;
		} catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public boolean logout(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			final SessionBean session = sessionDao.load(sessionIdentifier);
			if (session != null && session.getCurrentUser() != null) {
				session.setCurrentUser(null);
				sessionDao.save(session);
				return true;
			}
			return false;
		} catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public UserIdentifier getCurrentUser(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			final SessionBean session = sessionDao.load(sessionIdentifier);
			if (session != null) {
				return session.getCurrentUser();
			}
			return null;
		} catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public UserIdentifier register(
		final SessionIdentifier sessionIdentifier, final String shortenUrl, final String validateEmailUrl, final String username, final String email,
		final String password
	) throws AuthenticationServiceException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			if (isLoggedIn(sessionIdentifier)) {
				final String msg = "can't register while logged in";
				logger.debug(msg);
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple(msg)));
			}
			final UserIdentifier userIdentifier = new UserIdentifier(username);
			final UserBean user = userDao.create();
			user.setId(userIdentifier);
			setNewEmail(user, email);
			setNewPassword(user, password);

			final ValidationResult errors = validationExecutor.validate(user);
			if (errors.hasErrors()) {
				logger.warn("User " + errors.toString());
				throw new ValidationException(errors);
			}

			sendEmailVerify(user, shortenUrl, validateEmailUrl);
			userDao.save(user);

			logger.info("registerd user " + user.getId());
			login(sessionIdentifier, user.getId(), password);
			return user.getId();
		} catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} catch (InvalidKeySpecException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} catch (ParseException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} catch (NoSuchAlgorithmException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} catch (ShortenerServiceException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} catch (MailServiceException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	private void setNewEmail(final UserBean user, final String email) {
		if (email != null && !email.equals(user.getEmail())) {
			user.setEmailNew(email);
			user.setEmailVerified(false);
			user.setEmailVerifyToken(String.valueOf(UUID.randomUUID()));
		}
	}

	private void sendEmailVerify(
		final UserBean user,
		final String shortenUrl,
		final String verifyUrl
	) throws MailServiceException, ShortenerServiceException, ParseException,
		ValidationException {
		if (Boolean.TRUE.equals(user.getEmailVerified())) {
			logger.trace("email already verified => skip send mail");
		} else if (user.getEmailNew() == null) {
			logger.warn("newEmail is null => skip send mail");
		} else {
			logger.debug("email not verified, sending mail");
			final String from = authenticationConfig.getEmailFrom();
			final String to = user.getEmailNew();
			final String subject = "Validate Email";
			final StringBuilder content = new StringBuilder();
			logger.debug("verifyUrl: " + verifyUrl);
			content.append(shortenLink(shortenUrl, String.format(verifyUrl, user.getEmailVerifyToken(), String.valueOf(user.getId()))));
			final String contentType = "text/plain";
			mailService.send(new MailDto(from, to, subject, content.toString(), contentType));
		}
	}

	private String shortenLink(final String shortenUrl, final String link) throws ShortenerServiceException, ParseException, ValidationException {
		logger.debug("shortenLink - shortenUrl: " + shortenUrl + " link: " + link);
		final ShortenerUrlIdentifier token = shortenerService.shorten(parseUtil.parseURL(link));
		return String.format(shortenUrl, String.valueOf(token));
	}

	@Override
	public boolean unregister(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			final UserIdentifier userIdentifier = getCurrentUser(sessionIdentifier);
			if (userIdentifier == null) {
				return false;
			}
			final UserBean user = userDao.load(userIdentifier);
			if (user == null) {
				return false;
			}
			final boolean result = logout(sessionIdentifier);
			userDao.delete(user);
			return result;
		} catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public boolean changePassword(
		final SessionIdentifier sessionIdentifier,
		final String currentPassword,
		final String newPassword,
		final String newPasswordRepeat
	)
		throws AuthenticationServiceException, LoginRequiredException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectLoggedIn(sessionIdentifier);
			if (!newPassword.equals(newPasswordRepeat)) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("passwordRepeat not matching")));
			}
			final UserIdentifier userIdentifier = getCurrentUser(sessionIdentifier);
			if (userIdentifier == null) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("currentuser not found")));
			}
			if (!verifyCredential(userIdentifier, currentPassword)) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("password not match currentpassword")));
			}
			final UserBean user = userDao.load(userIdentifier);
			if (user == null) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("user not found")));
			}

			setNewPassword(user, newPassword);

			final ValidationResult errors = validationExecutor.validate(user);
			if (errors.hasErrors()) {
				logger.warn("User " + errors.toString());
				throw new ValidationException(errors);
			}
			userDao.save(user);

			logger.info("set password => true");
			return true;
		} catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} catch (InvalidKeySpecException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} catch (NoSuchAlgorithmException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	private void setNewPassword(final UserBean user, final String newPassword) throws NoSuchAlgorithmException, InvalidKeySpecException, ValidationException {
		final List<ValidationError> errors = authenticationPasswordUtil.validatePassword(newPassword);
		if (!errors.isEmpty()) {
			throw new ValidationException(new ValidationResultImpl(errors));
		}
		final byte[] newSalt = passwordEncryptionService.generateSalt();
		final byte[] newEncryptedPassword = passwordEncryptionService.encryptPassword(newPassword, newSalt);
		user.setPassword(newEncryptedPassword);
		user.setPasswordSalt(newSalt);
		user.setEmailVerifyToken(String.valueOf(UUID.randomUUID()));
	}

	@Override
	public UserIdentifier createUserIdentifier(final String username) throws AuthenticationServiceException {
		if (username == null || username.trim().isEmpty()) {
			return null;
		} else {
			return new UserIdentifier(username);
		}
	}

	@Override
	public SessionIdentifier createSessionIdentifier(final HttpServletRequest request) throws AuthenticationServiceException {
		return new SessionIdentifier(request.getSession().getId());
	}

	@Override
	public Collection<UserIdentifier> userList(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectLoggedIn(sessionIdentifier);
			final Set<UserIdentifier> result = new HashSet<UserIdentifier>();
			final IdentifierIterator<UserIdentifier> i = userDao.getIdentifierIterator();
			while (i.hasNext()) {
				result.add(i.next());
			}
			return result;
		} catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} catch (IdentifierIteratorException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public boolean existsUser(final UserIdentifier userIdentifier) throws AuthenticationServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			return authenticationVerifyCredential.existsUser(userIdentifier);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public boolean existsSession(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			return sessionDao.exists(sessionIdentifier);
		} catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void expectLoggedIn(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			if (!isLoggedIn(sessionIdentifier)) {
				throw new LoginRequiredException("user not logged in");
			}
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public String getFullname(final UserIdentifier userIdentifier) throws AuthenticationServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			return authenticationVerifyCredential.getFullname(userIdentifier);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public User getUser(final UserIdentifier userIdentifier) throws AuthenticationServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			return authenticationVerifyCredential.getUser(userIdentifier);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public SessionIdentifier createSystemUser(final String systemUsername) throws AuthenticationServiceException {
		try {
			final UserIdentifier userIdentifier = new UserIdentifier(systemUsername);
			final UserBean userBean;
			if (userDao.exists(userIdentifier)) {
				userBean = userDao.load(userIdentifier);
			} else {
				userBean = userDao.create();
				userBean.setId(userIdentifier);
				userBean.setSystemUser(true);
				userDao.save(userBean);
			}
			return new SessionIdentifier(systemUsername);
		} catch (StorageException e) {
			throw new AuthenticationServiceException(e);
		}
	}

	@Override
	public void switchUser(
		final SessionIdentifier sessionIdentifier,
		final UserIdentifier userIdentifier
	) throws AuthenticationServiceException, LoginRequiredException,
		SuperAdminRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectSuperAdmin(sessionIdentifier);
			final SessionBean session = sessionDao.findOrCreate(sessionIdentifier);
			session.setCurrentUser(userIdentifier);
			sessionDao.save(session);
		} catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void expectSuperAdmin(final SessionIdentifier sessionIdentifier) throws SuperAdminRequiredException, AuthenticationServiceException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectLoggedIn(sessionIdentifier);
			if (!isSuperAdmin(sessionIdentifier)) {
				throw new SuperAdminRequiredException("no superadmin!");
			}
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public boolean isSuperAdmin(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			final UserIdentifier currentUser = getCurrentUser(sessionIdentifier);

			if (currentUser == null) {
				return false;
			}
			return isSuperAdmin(currentUser);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public boolean isSuperAdmin(final UserIdentifier userIdentifier) throws AuthenticationServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			if (userIdentifier == null) {
				return false;
			}
			if (AuthenticationConstants.SUPERADMIN_USER_NAME.equals(userIdentifier.getId())) {
				return true;
			}
			final UserBean user = userDao.load(userIdentifier);
			if (user == null) {
				return false;
			}
			return Boolean.TRUE.equals(user.getSuperAdmin());
		} catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public TimeZone getTimeZone(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			if (isLoggedIn(sessionIdentifier)) {
				final UserIdentifier userIdentifier = getCurrentUser(sessionIdentifier);
				final UserBean user = userDao.load(userIdentifier);
				if (user != null) {
					final TimeZone timeZone = user.getTimeZone();
					if (timeZone != null) {
						return timeZone;
					}
				}
			}
			return timeZoneUtil.getUTCTimeZone();
		} catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void updateUser(
		final SessionIdentifier sessionIdentifier, final String shortenUrl, final String validateEmailUrl, final String email, final String fullname,
		final String timeZoneString
	) throws AuthenticationServiceException, LoginRequiredException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectLoggedIn(sessionIdentifier);

			final TimeZone timeZone;
			try {
				timeZone = timeZoneUtil.parseTimeZone(timeZoneString);
			} catch (final ParseException e) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("illegal timeZone")));
			}
			final UserIdentifier userIdentifier = getCurrentUser(sessionIdentifier);
			final UserBean user = userDao.load(userIdentifier);
			setNewEmail(user, email);
			user.setFullname(fullname);
			user.setTimeZone(timeZone);

			final ValidationResult errors = validationExecutor.validate(user);
			if (errors.hasErrors()) {
				logger.warn("User " + errors.toString());
				throw new ValidationException(errors);
			}

			sendEmailVerify(user, shortenUrl, validateEmailUrl);
			userDao.save(user);
		} catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} catch (ParseException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} catch (MailServiceException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} catch (ShortenerServiceException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public boolean login(
		final SessionIdentifier sessionIdentifier,
		final String username,
		final String password
	) throws AuthenticationServiceException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			return login(sessionIdentifier, createUserIdentifier(username), password);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public boolean verifyEmailToken(final UserIdentifier userIdentifier, final String token) throws AuthenticationServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			final UserBean user = userDao.load(userIdentifier);
			if (user != null && token != null && token.equals(user.getEmailVerifyToken())) {
				user.setEmailVerified(true);
				user.setEmail(user.getEmailNew());
				user.setEmailNew(null);
				user.setEmailVerifyToken(null);
				userDao.save(user, new StorageValueList(userDao.getEncoding()).add("emailVerified").add("email").add("emailNew").add("emailVerifyToken"));
				return true;
			} else {
				return false;
			}
		} catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public UserIdentifier createUser(
		final SessionIdentifier sessionIdentifier,
		final UserDto userDto
	) throws AuthenticationServiceException, LoginRequiredException,
		ValidationException, SuperAdminRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectSuperAdmin(sessionIdentifier);

			final UserBean user = userDao.create();
			user.setId(userDto.getId());
			setNewEmail(user, userDto.getEmail());
			setNewPassword(user, authenticationPasswordUtil.generatePassword());

			final ValidationResult errors = validationExecutor.validate(user);
			if (errors.hasErrors()) {
				logger.warn("User " + errors.toString());
				throw new ValidationException(errors);
			}

			userDao.save(user);

			return user.getId();
		} catch (final NoSuchAlgorithmException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} catch (StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} catch (InvalidKeySpecException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} catch (AuthenticationGeneratePasswordFailedException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void deleteUser(
		final SessionIdentifier sessionIdentifier,
		final UserIdentifier userIdentifier
	) throws AuthenticationServiceException, LoginRequiredException,
		SuperAdminRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectSuperAdmin(sessionIdentifier);

			userDao.delete(userIdentifier);
		} catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void sendPasswordLostEmail(
		final String shortenUrl, final String resetUrl, final UserIdentifier userIdentifier,
		final String email
	) throws AuthenticationServiceException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			final UserBean user = userDao.load(userIdentifier);
			if (user == null) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("no such user found")));
			}
			if (email == null || !email.equals(user.getEmail())) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("email missmatch")));
			}
			sendPasswordLostEmail(user, shortenUrl, resetUrl);
		} catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} catch (ParseException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} catch (MailServiceException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} catch (ShortenerServiceException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	private void sendPasswordLostEmail(
		final UserBean user,
		final String shortenUrl,
		final String resetUrl
	) throws MailServiceException, ShortenerServiceException, ParseException,
		ValidationException {
		final String from = authenticationConfig.getEmailFrom();
		final String to = user.getEmail();
		final String subject = "Password Reset";
		final StringBuilder content = new StringBuilder();
		content.append(shortenLink(shortenUrl, String.format(resetUrl, user.getEmailVerifyToken(), String.valueOf(user.getId()))));
		final String contentType = "text/plain";
		mailService.send(new MailDto(from, to, subject, content.toString(), contentType));
	}

	@Override
	public void setNewPassword(
		final UserIdentifier userIdentifier, final String token, final String newPassword,
		final String newPasswordRepeat
	) throws AuthenticationServiceException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			if (!newPassword.equals(newPasswordRepeat)) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("passwordRepeat not matching")));
			}
			final UserBean user = userDao.load(userIdentifier);
			if (user == null) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("no such user found")));
			}
			if (user.getEmailVerifyToken() == null || !user.getEmailVerifyToken().equals(token)) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("token missmatch")));
			}
			setNewPassword(user, newPassword);

			final ValidationResult errors = validationExecutor.validate(user);
			if (errors.hasErrors()) {
				logger.warn("User " + errors.toString());
				throw new ValidationException(errors);
			}
			userDao.save(user);
		} catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} catch (InvalidKeySpecException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} catch (NoSuchAlgorithmException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}
}
