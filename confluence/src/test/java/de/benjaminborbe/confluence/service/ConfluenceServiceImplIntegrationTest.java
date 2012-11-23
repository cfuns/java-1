package de.benjaminborbe.confluence.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.confluence.ConfluenceTestConstants;
import de.benjaminborbe.confluence.api.ConfluenceInstance;
import de.benjaminborbe.confluence.api.ConfluenceInstanceIdentifier;
import de.benjaminborbe.confluence.api.ConfluenceService;
import de.benjaminborbe.confluence.guice.ConfluenceModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class ConfluenceServiceImplIntegrationTest {

	@Test
	public void testInject() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ConfluenceModulesMock());
		final ConfluenceService confluenceService = injector.getInstance(ConfluenceService.class);
		assertNotNull(confluenceService);
		assertEquals(ConfluenceServiceImpl.class.getName(), confluenceService.getClass().getName());
	}

	@Test
	public void testCreate() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ConfluenceModulesMock());
		final ConfluenceService confluenceService = injector.getInstance(ConfluenceService.class);
		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.replay(request);
		final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

		// register
		final UserIdentifier userIdentifier = authenticationService.createUserIdentifier(ConfluenceTestConstants.USER_NAME);
		assertNotNull(userIdentifier);
		assertEquals(ConfluenceTestConstants.USER_NAME, userIdentifier.getId());
		authenticationService.register(sessionIdentifier, ConfluenceTestConstants.USER_NAME, ConfluenceTestConstants.USER_EMAIL, ConfluenceTestConstants.USER_PASSWORD,
				ConfluenceTestConstants.USER_FULL, TimeZone.getDefault());

		// login
		assertTrue(authenticationService.login(sessionIdentifier, ConfluenceTestConstants.USER_NAME, ConfluenceTestConstants.USER_PASSWORD));
		assertEquals(userIdentifier, authenticationService.getCurrentUser(sessionIdentifier));

		// create
		final ConfluenceInstanceIdentifier confluenceInstanceIdentifier = confluenceService.createConfluenceIntance(sessionIdentifier, ConfluenceTestConstants.CONFLUENCE_HOSTNAME,
				ConfluenceTestConstants.CONFLUENCE_USERNAME, ConfluenceTestConstants.CONFLUENCE_PASSWORD);
		assertNotNull(confluenceInstanceIdentifier);

		// read
		{
			final ConfluenceInstance confluenceInstance = confluenceService.getConfluenceInstance(sessionIdentifier, confluenceInstanceIdentifier);
			assertNotNull(confluenceInstance);
			assertNotNull(confluenceInstance.getId());
			assertEquals(confluenceInstanceIdentifier, confluenceInstance.getId());
			assertNotNull(confluenceInstance.getUrl());
			assertEquals(ConfluenceTestConstants.CONFLUENCE_HOSTNAME, confluenceInstance.getUrl());
			assertNotNull(confluenceInstance.getUsername());
			assertEquals(ConfluenceTestConstants.CONFLUENCE_USERNAME, confluenceInstance.getUsername());
		}

		// update
		confluenceService.updateConfluenceIntance(sessionIdentifier, confluenceInstanceIdentifier, ConfluenceTestConstants.CONFLUENCE_HOSTNAME + "bla",
				ConfluenceTestConstants.CONFLUENCE_USERNAME + "bla", ConfluenceTestConstants.CONFLUENCE_PASSWORD + "bla");

		// read
		{
			final ConfluenceInstance confluenceInstance = confluenceService.getConfluenceInstance(sessionIdentifier, confluenceInstanceIdentifier);
			assertNotNull(confluenceInstance);
			assertNotNull(confluenceInstance.getId());
			assertEquals(confluenceInstanceIdentifier, confluenceInstance.getId());
			assertNotNull(confluenceInstance.getUrl());
			assertEquals(ConfluenceTestConstants.CONFLUENCE_HOSTNAME + "bla", confluenceInstance.getUrl());
			assertNotNull(confluenceInstance.getUsername());
			assertEquals(ConfluenceTestConstants.CONFLUENCE_USERNAME + "bla", confluenceInstance.getUsername());
		}

		// delete
		confluenceService.deleteConfluenceInstance(sessionIdentifier, confluenceInstanceIdentifier);

		// read
		assertNull(confluenceService.getConfluenceInstance(sessionIdentifier, confluenceInstanceIdentifier));
	}
}
