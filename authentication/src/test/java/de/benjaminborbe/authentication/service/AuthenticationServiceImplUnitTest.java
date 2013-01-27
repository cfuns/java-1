package de.benjaminborbe.authentication.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authentication.dao.SessionBean;
import de.benjaminborbe.authentication.dao.SessionDao;
import de.benjaminborbe.authentication.dao.UserBean;
import de.benjaminborbe.authentication.dao.UserDao;
import de.benjaminborbe.authentication.util.AuthenticationPasswordEncryptionService;
import de.benjaminborbe.authentication.verifycredential.AuthenticationVerifyCredential;
import de.benjaminborbe.authentication.verifycredential.AuthenticationVerifyCredentialRegistry;
import de.benjaminborbe.authentication.verifycredential.AuthenticationVerifyCredentialStorage;
import de.benjaminborbe.tools.util.Duration;
import de.benjaminborbe.tools.util.DurationUtil;

public class AuthenticationServiceImplUnitTest {

	@Test
	public void testVerifyCredential() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final SessionDao sessionDao = EasyMock.createMock(SessionDao.class);
		EasyMock.replay(sessionDao);
		final byte[] encryptedPassword = "test123".getBytes();
		final byte[] salt = "salt".getBytes();
		final UserBean user = EasyMock.createMock(UserBean.class);
		EasyMock.expect(user.getPassword()).andReturn(encryptedPassword).anyTimes();
		EasyMock.expect(user.getPasswordSalt()).andReturn(salt).anyTimes();
		EasyMock.replay(user);

		final UserIdentifier userRight = new UserIdentifier("bborbe");
		final UserIdentifier userWrong = new UserIdentifier("wrong");

		final UserDao userDao = EasyMock.createMock(UserDao.class);
		EasyMock.expect(userDao.load(userRight)).andReturn(user).anyTimes();
		EasyMock.expect(userDao.load(userWrong)).andReturn(null).anyTimes();
		EasyMock.replay(userDao);

		final AuthenticationPasswordEncryptionService p = EasyMock.createMock(AuthenticationPasswordEncryptionService.class);
		EasyMock.expect(p.authenticate("test123", encryptedPassword, salt)).andReturn(true);
		EasyMock.expect(p.authenticate("wrongPw", encryptedPassword, salt)).andReturn(false);
		EasyMock.replay(p);

		final AuthenticationVerifyCredential v = new AuthenticationVerifyCredentialStorage(logger, userDao, p);
		final AuthenticationVerifyCredentialRegistry verifyCredentialRegistry = EasyMock.createMock(AuthenticationVerifyCredentialRegistry.class);
		EasyMock.expect(verifyCredentialRegistry.getAll()).andReturn(Arrays.asList(v)).anyTimes();
		EasyMock.replay(verifyCredentialRegistry);

		final Duration duration = EasyMock.createMock(Duration.class);
		EasyMock.expect(duration.getTime()).andReturn(1337l).anyTimes();
		EasyMock.replay(duration);

		final DurationUtil durationUtil = EasyMock.createMock(DurationUtil.class);
		EasyMock.expect(durationUtil.getDuration()).andReturn(duration).anyTimes();
		EasyMock.replay(durationUtil);

		final SessionIdentifier sessionIdentifier = EasyMock.createMock(SessionIdentifier.class);
		EasyMock.replay(sessionIdentifier);

		final AuthenticationService authenticationService = new AuthenticationServiceImpl(logger, null, null, null, sessionDao, userDao, verifyCredentialRegistry, p, null, null, null,
				durationUtil);
		assertFalse(authenticationService.verifyCredential(sessionIdentifier, userWrong, "test123"));
		assertFalse(authenticationService.verifyCredential(sessionIdentifier, userRight, "wrongPw"));
		assertTrue(authenticationService.verifyCredential(sessionIdentifier, userRight, "test123"));
	}

	@Test
	public void testGetCurrentUserExists() throws Exception {
		final String sessionId = "abc";
		final String username = "username";

		final HttpSession session = EasyMock.createMock(HttpSession.class);
		EasyMock.expect(session.getId()).andReturn(sessionId);
		EasyMock.replay(session);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getSession()).andReturn(session);
		EasyMock.replay(request);

		final SessionIdentifier sessionIdentifier = new SessionIdentifier(sessionId);

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final UserIdentifier userIdentifier = new UserIdentifier(username);

		final SessionBean sessionBean = EasyMock.createMock(SessionBean.class);
		EasyMock.expect(sessionBean.getCurrentUser()).andReturn(userIdentifier);
		EasyMock.replay(sessionBean);

		final SessionDao sessionDao = EasyMock.createMock(SessionDao.class);
		EasyMock.expect(sessionDao.load(sessionIdentifier)).andReturn(sessionBean);
		EasyMock.replay(sessionDao);

		final Duration duration = EasyMock.createMock(Duration.class);
		EasyMock.expect(duration.getTime()).andReturn(1337l).anyTimes();
		EasyMock.replay(duration);

		final DurationUtil durationUtil = EasyMock.createMock(DurationUtil.class);
		EasyMock.expect(durationUtil.getDuration()).andReturn(duration).anyTimes();
		EasyMock.replay(durationUtil);

		final UserDao userDao = EasyMock.createMock(UserDao.class);
		EasyMock.replay(userDao);

		final AuthenticationVerifyCredentialRegistry verifyCredentialRegistry = EasyMock.createMock(AuthenticationVerifyCredentialRegistry.class);
		EasyMock.replay(verifyCredentialRegistry);

		final AuthenticationService authenticationService = new AuthenticationServiceImpl(logger, null, null, null, sessionDao, userDao, verifyCredentialRegistry, null, null, null,
				null, durationUtil);
		assertEquals(username, authenticationService.getCurrentUser(sessionIdentifier).getId());
	}

	@Test
	public void testGetCurrentUserExistsNot() throws Exception {
		final String sessionId = "abc";

		final HttpSession session = EasyMock.createMock(HttpSession.class);
		EasyMock.expect(session.getId()).andReturn(sessionId);
		EasyMock.replay(session);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getSession()).andReturn(session);
		EasyMock.replay(request);

		final SessionIdentifier sessionIdentifier = new SessionIdentifier(sessionId);

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final Duration duration = EasyMock.createMock(Duration.class);
		EasyMock.expect(duration.getTime()).andReturn(1337l).anyTimes();
		EasyMock.replay(duration);

		final DurationUtil durationUtil = EasyMock.createMock(DurationUtil.class);
		EasyMock.expect(durationUtil.getDuration()).andReturn(duration).anyTimes();
		EasyMock.replay(durationUtil);

		final SessionDao sessionDao = EasyMock.createMock(SessionDao.class);
		EasyMock.expect(sessionDao.load(sessionIdentifier)).andReturn(null);
		EasyMock.replay(sessionDao);

		final UserDao userDao = EasyMock.createMock(UserDao.class);
		EasyMock.replay(userDao);

		final AuthenticationVerifyCredentialRegistry verifyCredentialRegistry = EasyMock.createMock(AuthenticationVerifyCredentialRegistry.class);
		EasyMock.replay(verifyCredentialRegistry);

		final AuthenticationService authenticationService = new AuthenticationServiceImpl(logger, null, null, null, sessionDao, userDao, verifyCredentialRegistry, null, null, null,
				null, durationUtil);
		assertNull(authenticationService.getCurrentUser(sessionIdentifier));
	}
}
