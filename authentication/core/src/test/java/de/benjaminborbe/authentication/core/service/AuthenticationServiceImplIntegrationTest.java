package de.benjaminborbe.authentication.core.service;

import com.google.inject.Injector;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authentication.core.dao.UserBean;
import de.benjaminborbe.authentication.core.dao.UserDao;
import de.benjaminborbe.authentication.core.guice.AuthenticationModulesMock;
import de.benjaminborbe.mail.mock.MailServiceMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.easymock.EasyMock;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.TimeZone;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class AuthenticationServiceImplIntegrationTest {

	private final String validateEmailBaseUrl = "http://example.com/test";

	private final String shortenUrl = "http://bb/bb/s";

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
	public void testRegisterSendEmailValidationEmail() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new AuthenticationModulesMock());
		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		final MailServiceMock mailService = injector.getInstance(MailServiceMock.class);
		final UserDao userDao = injector.getInstance(UserDao.class);
		final String sessionId = "sid123";

		final HttpSession session = EasyMock.createMock(HttpSession.class);
		EasyMock.expect(session.getId()).andReturn(sessionId);
		EasyMock.replay(session);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getSession()).andReturn(session);
		EasyMock.replay(request);

		final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
		final String username = "testuser";
		final String email = "test@example.com";
		final String password = "Test123!";
		final String fullname = null;
		final TimeZone timeZone = null;
		assertThat(mailService.getMails().size(), is(0));
		final UserIdentifier userIdentifier = authenticationService.register(sessionIdentifier, shortenUrl, validateEmailBaseUrl, username, email, password);
		assertNotNull(userIdentifier);
		assertEquals(username, userIdentifier.getId());
		assertThat(mailService.getMails().size(), is(1));
		assertEquals(email, mailService.getMails().get(0).getTo());

		final UserBean userWithoutVerifiedEmail = userDao.load(userIdentifier);
		assertNotNull(userWithoutVerifiedEmail);
		assertNull(userWithoutVerifiedEmail.getEmail());
		assertEquals(email, userWithoutVerifiedEmail.getEmailNew());
		assertEquals(false, userWithoutVerifiedEmail.getEmailVerified());
		assertNotNull(userWithoutVerifiedEmail.getEmailVerifyToken());

		authenticationService.verifyEmailToken(userIdentifier, userWithoutVerifiedEmail.getEmailVerifyToken());

		final UserBean userWithVerifiedEmail = userDao.load(userIdentifier);
		assertNotNull(userWithVerifiedEmail);
		assertEquals(email, userWithVerifiedEmail.getEmail());
		assertNull(userWithVerifiedEmail.getEmailNew());
		assertEquals(true, userWithVerifiedEmail.getEmailVerified());
		assertNull(userWithVerifiedEmail.getEmailVerifyToken());
	}
}
