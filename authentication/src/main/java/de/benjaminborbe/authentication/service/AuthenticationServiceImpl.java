package de.benjaminborbe.authentication.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.api.ValidationException;
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
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.storage.tools.IdentifierIteratorException;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.validation.ValidationResultImpl;

@Singleton
public class AuthenticationServiceImpl implements AuthenticationService {

	private final Logger logger;

	private final SessionDao sessionDao;

	private final UserDao userDao;

	private final AuthenticationVerifyCredentialRegistry verifyCredentialRegistry;

	private final AuthenticationPasswordEncryptionService passwordEncryptionService;

	private final TimeZoneUtil timeZoneUtil;

	@Inject
	public AuthenticationServiceImpl(
			final Logger logger,
			final SessionDao sessionDao,
			final UserDao userDao,
			final AuthenticationVerifyCredentialRegistry verifyCredentialRegistry,
			final AuthenticationPasswordEncryptionService passwordEncryptionService,
			final TimeZoneUtil timeZoneUtil) {
		this.logger = logger;
		this.sessionDao = sessionDao;
		this.userDao = userDao;
		this.verifyCredentialRegistry = verifyCredentialRegistry;
		this.passwordEncryptionService = passwordEncryptionService;
		this.timeZoneUtil = timeZoneUtil;
	}

	@Override
	public boolean verifyCredential(final SessionIdentifier sessionIdentifier, final UserIdentifier userIdentifier, final String password) throws AuthenticationServiceException {
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
				logger.trace("login success");
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
	public UserIdentifier register(final SessionIdentifier sessionIdentifier, final String username, final String email, final String password, final String fullname,
			final TimeZone timeZone) throws AuthenticationServiceException, ValidationException {
		try {
			final UserIdentifier userIdentifier = new UserIdentifier(username);
			if (userDao.load(userIdentifier) != null) {
				logger.info("user " + userIdentifier + " already exists");
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("user already exists")));
			}
			final UserBean user = userDao.create();
			user.setId(userIdentifier);
			user.setEmail(email);
			setNewPassword(user, password);
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
	public boolean changePassword(final SessionIdentifier sessionIdentifier, final String currentPassword, final String newPassword) throws AuthenticationServiceException {
		try {
			final UserIdentifier userIdentifier = getCurrentUser(sessionIdentifier);
			if (userIdentifier == null) {
				return false;
			}
			final UserBean user = userDao.load(userIdentifier);
			if (user == null) {
				return false;
			}
			if (verifyCredential(sessionIdentifier, userIdentifier, currentPassword)) {
				return false;
			}
			setNewPassword(user, newPassword);
			userDao.save(user);
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

	private void setNewPassword(final UserBean user, final String newPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
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
			return userDao.exists(userIdentifier);
		}
		catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
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
		return isSuperAdmin(currentUser);
	}

	@Override
	public boolean isSuperAdmin(final UserIdentifier userIdentifier) throws AuthenticationServiceException {
		try {
			final UserBean user = userDao.load(userIdentifier);
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
				final TimeZone timeZone = user.getTimeZone();
				if (timeZone != null) {
					return timeZone;
				}
			}
		}
		catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
		return timeZoneUtil.getUTCTimeZone();
	}

	@Override
	public void updateUser(final SessionIdentifier sessionIdentifier, final String email, final String fullname, final String timeZoneString) throws AuthenticationServiceException,
			LoginRequiredException, ValidationException {
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
			user.setEmail(email);
			user.setFullname(fullname);
			user.setTimeZone(timeZone);
			userDao.save(user);
		}
		catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public boolean login(final SessionIdentifier sessionIdentifier, final String username, final String password) throws AuthenticationServiceException, ValidationException {
		return login(sessionIdentifier, createUserIdentifier(username), password);
	}
}
