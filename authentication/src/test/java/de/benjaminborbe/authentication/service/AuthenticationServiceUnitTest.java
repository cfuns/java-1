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
import de.benjaminborbe.authentication.session.SessionBean;
import de.benjaminborbe.authentication.session.SessionDao;
import de.benjaminborbe.authentication.user.UserBean;
import de.benjaminborbe.authentication.user.UserDao;
import de.benjaminborbe.authentication.verifycredential.VerifyCredential;
import de.benjaminborbe.authentication.verifycredential.VerifyCredentialRegistry;
import de.benjaminborbe.authentication.verifycredential.VerifyCredentialStorage;

public class AuthenticationServiceUnitTest {

	@Test
	public void testVerifyCredential() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final SessionDao sessionDao = EasyMock.createMock(SessionDao.class);
		EasyMock.replay(sessionDao);

		final UserBean user = EasyMock.createMock(UserBean.class);
		EasyMock.expect(user.getPassword()).andReturn("test123").anyTimes();
		EasyMock.replay(user);

		final UserIdentifier userRight = new UserIdentifier("bborbe");
		final UserIdentifier userWrong = new UserIdentifier("wrong");

		final UserDao userDao = EasyMock.createMock(UserDao.class);
		EasyMock.expect(userDao.load(userRight)).andReturn(user).anyTimes();
		EasyMock.expect(userDao.load(userWrong)).andReturn(null).anyTimes();
		EasyMock.replay(userDao);

		final VerifyCredential v = new VerifyCredentialStorage(logger, userDao);
		final VerifyCredentialRegistry verifyCredentialRegistry = EasyMock.createMock(VerifyCredentialRegistry.class);
		EasyMock.expect(verifyCredentialRegistry.getAll()).andReturn(Arrays.asList(v)).anyTimes();
		EasyMock.replay(verifyCredentialRegistry);

		final AuthenticationService authenticationService = new AuthenticationServiceImpl(logger, sessionDao, userDao, verifyCredentialRegistry);
		assertFalse(authenticationService.verifyCredential(userWrong, "test123"));
		assertFalse(authenticationService.verifyCredential(userRight, "wrong"));
		assertTrue(authenticationService.verifyCredential(userRight, "test123"));
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

		final UserDao userDao = EasyMock.createMock(UserDao.class);
		EasyMock.replay(userDao);

		final VerifyCredentialRegistry verifyCredentialRegistry = EasyMock.createMock(VerifyCredentialRegistry.class);
		EasyMock.replay(verifyCredentialRegistry);

		final AuthenticationService authenticationService = new AuthenticationServiceImpl(logger, sessionDao, userDao, verifyCredentialRegistry);
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

		final SessionDao sessionDao = EasyMock.createMock(SessionDao.class);
		EasyMock.expect(sessionDao.load(sessionIdentifier)).andReturn(null);
		EasyMock.replay(sessionDao);

		final UserDao userDao = EasyMock.createMock(UserDao.class);
		EasyMock.replay(userDao);

		final VerifyCredentialRegistry verifyCredentialRegistry = EasyMock.createMock(VerifyCredentialRegistry.class);
		EasyMock.replay(verifyCredentialRegistry);

		final AuthenticationService authenticationService = new AuthenticationServiceImpl(logger, sessionDao, userDao, verifyCredentialRegistry);
		assertNull(authenticationService.getCurrentUser(sessionIdentifier));
	}
}
