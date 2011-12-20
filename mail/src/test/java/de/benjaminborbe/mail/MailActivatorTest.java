package de.benjaminborbe.mail;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.mail.guice.MailModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class MailActivatorTest {

	@Test
	public void Inject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MailModulesMock());
		final MailActivator o = injector.getInstance(MailActivator.class);
		assertNotNull(o);
	}
}
