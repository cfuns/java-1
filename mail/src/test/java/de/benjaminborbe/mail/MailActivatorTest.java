package de.benjaminborbe.mail;

import junit.framework.TestCase;

import com.google.inject.Injector;

import de.benjaminborbe.mail.guice.MailModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class MailActivatorTest extends TestCase {

	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MailModulesMock());
		final MailActivator o = injector.getInstance(MailActivator.class);
		assertNotNull(o);
	}
}
