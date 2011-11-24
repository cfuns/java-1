package de.benjaminborbe.mail;

import com.google.inject.Injector;

import de.benjaminborbe.mail.MailActivator;
import de.benjaminborbe.mail.guice.MailModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import junit.framework.TestCase;

public class MailActivatorTest extends TestCase {

	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MailModulesMock());
		final MailActivator o = injector.getInstance(MailActivator.class);
		assertNotNull(o);
	}
}
