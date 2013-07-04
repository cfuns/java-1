package de.benjaminborbe.mail.service;

import com.google.inject.Injector;
import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.mail.guice.MailModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class MailServiceIntegrationTest {

	@Ignore
	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MailModulesMock());
		final MailService mailService = injector.getInstance(MailService.class);
		assertNotNull(mailService);
	}
}
