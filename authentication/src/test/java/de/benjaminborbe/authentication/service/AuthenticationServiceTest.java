package de.benjaminborbe.authentication.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import com.google.inject.Injector;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.guice.AuthenticationModulesMock;
import de.benjaminborbe.authentication.util.SessionBean;
import de.benjaminborbe.authentication.util.SessionDao;
import de.benjaminborbe.authentication.util.UserBean;
import de.benjaminborbe.authentication.util.UserDao;
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
	public void testVerifyCredential() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final SessionDao sessionDao = EasyMock.createMock(SessionDao.class);
		EasyMock.replay(sessionDao);

		final UserBean user = EasyMock.createMock(UserBean.class);
		EasyMock.expect(user.getPassword()).andReturn("test123").anyTimes();
		EasyMock.replay(user);

		final UserDao userDao = EasyMock.createMock(UserDao.class);
		EasyMock.expect(userDao.findByUsername("bborbe")).andReturn(user).anyTimes();
		EasyMock.expect(userDao.findByUsername("wrong")).andReturn(null).anyTimes();
		EasyMock.replay(userDao);

		final AuthenticationService authenticationService = new AuthenticationServiceImpl(logger, sessionDao, userDao);
		assertFalse(authenticationService.verifyCredential("wrong", "test123"));
		assertFalse(authenticationService.verifyCredential("bborbe", "wrong"));
		assertTrue(authenticationService.verifyCredential("bborbe", "test123"));
	}

	@Test
	public void testGetCurrentUserExists() {
		final String sessionId = "abc";
		final String username = "username";

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final SessionBean session = EasyMock.createMock(SessionBean.class);
		EasyMock.expect(session.getCurrentUser()).andReturn(username);
		EasyMock.replay(session);

		final SessionDao sessionDao = EasyMock.createMock(SessionDao.class);
		EasyMock.expect(sessionDao.findBySessionId(sessionId)).andReturn(session);
		EasyMock.replay(sessionDao);

		final UserDao userDao = EasyMock.createMock(UserDao.class);
		EasyMock.replay(userDao);

		final AuthenticationService authenticationService = new AuthenticationServiceImpl(logger, sessionDao, userDao);
		assertEquals(username, authenticationService.getCurrentUser(sessionId));
	}

	@Test
	public void testGetCurrentUserExistsNot() {
		final String sessionId = "abc";

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final SessionDao sessionDao = EasyMock.createMock(SessionDao.class);
		EasyMock.expect(sessionDao.findBySessionId(sessionId)).andReturn(null);
		EasyMock.replay(sessionDao);

		final UserDao userDao = EasyMock.createMock(UserDao.class);
		EasyMock.replay(userDao);

		final AuthenticationService authenticationService = new AuthenticationServiceImpl(logger, sessionDao, userDao);
		assertNull(authenticationService.getCurrentUser(sessionId));
	}
}
