package de.benjaminborbe.authentication.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import com.google.inject.Injector;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authentication.guice.AuthenticationModulesMock;
import de.benjaminborbe.authentication.session.SessionBean;
import de.benjaminborbe.authentication.session.SessionDao;
import de.benjaminborbe.authentication.user.UserBean;
import de.benjaminborbe.authentication.user.UserDao;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class AuthenticationServiceTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new AuthenticationModulesMock());
		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		assertEquals(AuthenticationServiceImpl.class, authenticationService.getClass());
	}

	@Test
	public void testSingleton() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new AuthenticationModulesMock());
		final AuthenticationService a = injector.getInstance(AuthenticationService.class);
		final AuthenticationService b = injector.getInstance(AuthenticationService.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
	}

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

		final AuthenticationService authenticationService = new AuthenticationServiceImpl(logger, sessionDao, userDao);
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

		final AuthenticationService authenticationService = new AuthenticationServiceImpl(logger, sessionDao, userDao);
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

		final AuthenticationService authenticationService = new AuthenticationServiceImpl(logger, sessionDao, userDao);
		assertNull(authenticationService.getCurrentUser(sessionIdentifier));
	}
}
