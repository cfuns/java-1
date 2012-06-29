package de.benjaminborbe.authentication.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.guice.AuthenticationModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class AuthenticationServiceIntegrationTest {

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
}
