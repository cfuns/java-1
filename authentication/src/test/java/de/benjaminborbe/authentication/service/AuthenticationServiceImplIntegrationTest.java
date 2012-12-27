package de.benjaminborbe.authentication.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.guice.AuthenticationModulesMock;
import de.benjaminborbe.mail.mock.MailServiceMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class AuthenticationServiceImplIntegrationTest {

	private final String validateEmailBaseUrl = "http://example.com/test";

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
		authenticationService.register(sessionIdentifier, validateEmailBaseUrl, username, email, password, fullname, timeZone);
		assertThat(mailService.getMails().size(), is(1));
	}
}
