package de.benjaminborbe.authentication.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;
import de.benjaminborbe.authentication.AuthenticationConstants;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.SuperAdminRequiredException;
import de.benjaminborbe.authentication.api.User;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authentication.session.SessionBean;
import de.benjaminborbe.authentication.session.SessionDao;
import de.benjaminborbe.authentication.user.UserBean;
import de.benjaminborbe.authentication.user.UserDao;
import de.benjaminborbe.authentication.util.AuthenticationPasswordEncryptionService;
import de.benjaminborbe.authentication.verifycredential.AuthenticationVerifyCredential;
import de.benjaminborbe.authentication.verifycredential.AuthenticationVerifyCredentialRegistry;
import de.benjaminborbe.mail.api.Mail;
import de.benjaminborbe.mail.api.MailServiceException;
import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.storage.tools.IdentifierIteratorException;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.password.PasswordValidator;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.validation.ValidationExecutor;
import de.benjaminborbe.tools.validation.ValidationResultImpl;

@Singleton
public class AuthenticationServiceImpl implements AuthenticationService {

	private final Logger logger;

	private final SessionDao sessionDao;

	private final UserDao userDao;

	private final AuthenticationVerifyCredentialRegistry verifyCredentialRegistry;

	private final AuthenticationPasswordEncryptionService passwordEncryptionService;

	private final TimeZoneUtil timeZoneUtil;

	private final PasswordValidator passwordValidator;

	private final ValidationExecutor validationExecutor;

	private final MailService mailService;

	@Inject
	public AuthenticationServiceImpl(
			final Logger logger,
			final MailService mailService,
			final PasswordValidator passwordValidator,
			final SessionDao sessionDao,
			final UserDao userDao,
			final AuthenticationVerifyCredentialRegistry verifyCredentialRegistry,
			final AuthenticationPasswordEncryptionService passwordEncryptionService,
			final TimeZoneUtil timeZoneUtil,
			final ValidationExecutor validationExecutor) {
		this.logger = logger;
		this.mailService = mailService;
		this.passwordValidator = passwordValidator;
		this.sessionDao = sessionDao;
		this.userDao = userDao;
		this.verifyCredentialRegistry = verifyCredentialRegistry;
		this.passwordEncryptionService = passwordEncryptionService;
		this.timeZoneUtil = timeZoneUtil;
		this.validationExecutor = validationExecutor;
	}

	@Override
	public boolean verifyCredential(final SessionIdentifier sessionIdentifier, final UserIdentifier userIdentifier, final String password) throws AuthenticationServiceException {
		try {
			// delay login
			Thread.sleep(AuthenticationConstants.LOGIN_DELAY);

			for (final AuthenticationVerifyCredential a : verifyCredentialRegistry.getAll()) {
				try {
					if (a.verifyCredential(userIdentifier, password)) {
						return true;
					}
				}
				catch (final AuthenticationServiceException e) {
					logger.warn(e.getClass().getName(), e);
				}
			}
		}
		catch (final InterruptedException e) {
		}
		return false;
	}

	@Override
	public boolean login(final SessionIdentifier sessionIdentifier, final UserIdentifier userIdentifier, final String password) throws AuthenticationServiceException,
			ValidationException {
		try {
			if (verifyCredential(sessionIdentifier, userIdentifier, password)) {
				final SessionBean session = sessionDao.findOrCreate(sessionIdentifier);
				session.setCurrentUser(userIdentifier);
				sessionDao.save(session);
				logger.info("user " + userIdentifier + " logged in successful");
				return true;
			}
			else {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("login failed")));
			}
		}
		catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public boolean isLoggedIn(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException {
		try {
			final SessionBean session = sessionDao.load(sessionIdentifier);
			return session != null && session.getCurrentUser() != null;
		}
		catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public boolean logout(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException {
		try {
			final SessionBean session = sessionDao.load(sessionIdentifier);
			if (session != null && session.getCurrentUser() != null) {
				session.setCurrentUser(null);
				sessionDao.save(session);
				return true;
			}
			return false;
		}
		catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public UserIdentifier getCurrentUser(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException {
		try {
			final SessionBean session = sessionDao.load(sessionIdentifier);
			if (session != null) {
				return session.getCurrentUser();
			}
			return null;
		}
		catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public UserIdentifier register(final SessionIdentifier sessionIdentifier, final String validateEmailBaseUrl, final String username, final String email, final String password,
			final String fullname, final TimeZone timeZone) throws AuthenticationServiceException, ValidationException {
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

			sendEmailVerify(user, validateEmailBaseUrl);
			userDao.save(user);

			logger.info("registerd user " + userIdentifier);
			login(sessionIdentifier, userIdentifier, password);
			return userIdentifier;
		}
		catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final NoSuchAlgorithmException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final InvalidKeySpecException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final MailServiceException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	private void setNewEmail(final UserBean user, final String email) {
		if (email != null && !email.equals(user.getEmail())) {
			user.setEmail(email);
			user.setEmailVerified(false);
			user.setEmailVerifyToken(String.valueOf(UUID.randomUUID()));
		}
	}

	private void sendEmailVerify(final UserBean user, final String baseUrl) throws MailServiceException {
		if (Boolean.TRUE.equals(user.getEmailVerified())) {
			logger.debug("email already verified");
		}
		else {
			logger.debug("email not verified, sending mail");
			final String from = "bborbe@seibert-media.net";
			final String to = user.getEmail();
			final String subject = "Validate Email";
			final String content = String.format(baseUrl, user.getEmailVerifyToken(), String.valueOf(user.getId()));
			final String contentType = "text/plain";
			mailService.send(new Mail(from, to, subject, content, contentType));
		}
	}

	@Override
	public boolean unregister(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException {
		try {
			final UserIdentifier userIdentifier = getCurrentUser(sessionIdentifier);
			if (userIdentifier == null) {
				return false;
			}
			final UserBean user = userDao.load(userIdentifier);
			if (user == null) {
				return false;
			}
			userDao.delete(user);
			return logout(sessionIdentifier);
		}
		catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public boolean changePassword(final SessionIdentifier sessionIdentifier, final String currentPassword, final String newPassword, final String newPasswordRepeat)
			throws AuthenticationServiceException, LoginRequiredException, ValidationException {
		try {
			expectLoggedIn(sessionIdentifier);
			if (!newPassword.equals(newPasswordRepeat)) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("passwordRepeat not matching")));
			}
			final UserIdentifier userIdentifier = getCurrentUser(sessionIdentifier);
			if (userIdentifier == null) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("currentuser not found")));
			}
			if (!verifyCredential(sessionIdentifier, userIdentifier, currentPassword)) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("password not match currentpassword")));
			}
			final UserBean user = userDao.load(userIdentifier);
			if (user == null) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("user not found")));
			}

			setNewPassword(user, newPassword);
			userDao.save(user);
			logger.info("set password => true");
			return true;
		}
		catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final NoSuchAlgorithmException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final InvalidKeySpecException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	private void setNewPassword(final UserBean user, final String newPassword) throws NoSuchAlgorithmException, InvalidKeySpecException, ValidationException {
		final List<ValidationError> errors = new ArrayList<ValidationError>();
		if (!passwordValidator.hasDigest(newPassword, 1)) {
			errors.add(new ValidationErrorSimple("password contains no digest characters"));
		}
		if (!passwordValidator.hasLength(newPassword, 8)) {
			errors.add(new ValidationErrorSimple("password at least 8 characters"));
		}
		if (!passwordValidator.hasValidChars(newPassword)) {
			errors.add(new ValidationErrorSimple("password contains invalid characters"));
		}
		if (!passwordValidator.hasLowerCharacter(newPassword, 1)) {
			errors.add(new ValidationErrorSimple("password at least one lower letter"));
		}
		if (!passwordValidator.hasSpecialCharacter(newPassword, 1)) {
			errors.add(new ValidationErrorSimple("password at least one special character"));
		}
		if (!passwordValidator.hasUpperCharacter(newPassword, 1)) {
			errors.add(new ValidationErrorSimple("password at least one upper letter"));
		}
		if (!errors.isEmpty()) {
			throw new ValidationException(new ValidationResultImpl(errors));
		}
		final byte[] newSalt = passwordEncryptionService.generateSalt();
		final byte[] newEncryptedPassword = passwordEncryptionService.getEncryptedPassword(newPassword, newSalt);
		user.setPassword(newEncryptedPassword);
		user.setPasswordSalt(newSalt);
	}

	@Override
	public UserIdentifier createUserIdentifier(final String username) throws AuthenticationServiceException {
		return new UserIdentifier(username);
	}

	@Override
	public SessionIdentifier createSessionIdentifier(final HttpServletRequest request) throws AuthenticationServiceException {
		return new SessionIdentifier(request.getSession().getId());
	}

	@Override
	public Collection<UserIdentifier> userList(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException, LoginRequiredException {
		try {
			expectLoggedIn(sessionIdentifier);
			final Set<UserIdentifier> result = new HashSet<UserIdentifier>();
			final IdentifierIterator<UserIdentifier> i = userDao.getIdentifierIterator();
			while (i.hasNext()) {
				result.add(i.next());
			}
			return result;
		}
		catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final IdentifierIteratorException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public boolean existsUser(final UserIdentifier userIdentifier) throws AuthenticationServiceException {
		try {
			for (final AuthenticationVerifyCredential a : verifyCredentialRegistry.getAll()) {
				try {
					if (a.existsUser(userIdentifier)) {
						return true;
					}
				}
				catch (final AuthenticationServiceException e) {
					logger.warn(e.getClass().getName(), e);
				}
			}
			return false;
		}
		finally {
		}
	}

	@Override
	public boolean existsSession(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException {
		try {
			return sessionDao.exists(sessionIdentifier);
		}
		catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public void expectLoggedIn(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException, LoginRequiredException {
		if (!isLoggedIn(sessionIdentifier)) {
			throw new LoginRequiredException("user not logged in");
		}
	}

	@Override
	public String getFullname(final SessionIdentifier sessionIdentifier, final UserIdentifier userIdentifier) throws AuthenticationServiceException {
		for (final AuthenticationVerifyCredential a : verifyCredentialRegistry.getAll()) {
			final String username = a.getFullname(userIdentifier);
			if (username != null && username.length() > 0) {
				return username;
			}
		}
		return null;
	}

	@Override
	public User getUser(final SessionIdentifier sessionIdentifier, final UserIdentifier userIdentifier) throws AuthenticationServiceException, LoginRequiredException {
		try {
			expectLoggedIn(sessionIdentifier);
			return userDao.load(userIdentifier);
		}
		catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public void switchUser(final SessionIdentifier sessionIdentifier, final UserIdentifier userIdentifier) throws AuthenticationServiceException, LoginRequiredException,
			SuperAdminRequiredException {
		try {
			expectSuperAdmin(sessionIdentifier);
			final SessionBean session = sessionDao.findOrCreate(sessionIdentifier);
			session.setCurrentUser(userIdentifier);
			sessionDao.save(session);
		}
		catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public void expectSuperAdmin(final SessionIdentifier sessionIdentifier) throws SuperAdminRequiredException, AuthenticationServiceException, LoginRequiredException {
		expectLoggedIn(sessionIdentifier);
		if (!isSuperAdmin(sessionIdentifier)) {
			throw new SuperAdminRequiredException("no superadmin!");
		}
	}

	@Override
	public boolean isSuperAdmin(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException {
		final UserIdentifier currentUser = getCurrentUser(sessionIdentifier);
		if (currentUser == null) {
			return false;
		}
		return isSuperAdmin(currentUser);
	}

	@Override
	public boolean isSuperAdmin(final UserIdentifier userIdentifier) throws AuthenticationServiceException {
		try {
			final UserBean user = userDao.load(userIdentifier);
			if (user == null) {
				return false;
			}
			return Boolean.TRUE.equals(user.getSuperAdmin());
		}
		catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public TimeZone getTimeZone(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException {
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
		}
		catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public void updateUser(final SessionIdentifier sessionIdentifier, final String validateEmailBaseUrl, final String email, final String fullname, final String timeZoneString)
			throws AuthenticationServiceException, LoginRequiredException, ValidationException {
		try {
			expectLoggedIn(sessionIdentifier);

			final TimeZone timeZone;
			try {
				timeZone = timeZoneUtil.parseTimeZone(timeZoneString);
			}
			catch (final ParseException e) {
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

			sendEmailVerify(user, validateEmailBaseUrl);
			userDao.save(user);
		}
		catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final MailServiceException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public boolean login(final SessionIdentifier sessionIdentifier, final String username, final String password) throws AuthenticationServiceException, ValidationException {
		return login(sessionIdentifier, createUserIdentifier(username), password);
	}

	@Override
	public boolean verifyEmail(final UserIdentifier userIdentifier, final String token) throws AuthenticationServiceException {
		try {
			final UserBean user = userDao.load(userIdentifier);
			if (user != null && token != null && token.equals(user.getEmailVerifyToken())) {
				user.setEmailVerified(true);
				userDao.save(user);
				return true;
			}
			else {
				return false;
			}
		}
		catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
	}
}
