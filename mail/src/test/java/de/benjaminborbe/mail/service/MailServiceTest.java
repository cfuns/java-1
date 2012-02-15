package de.benjaminborbe.mail.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.mail.guice.MailModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class MailServiceTest {

	@Ignore
	@Test
	public void testinject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MailModulesMock());
		final MailService o = injector.getInstance(MailService.class);
		assertNotNull(o);
	}
}
