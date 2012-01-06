package de.benjaminborbe.mail.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.mail.guice.MailModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class MailDashboardWidgetTest {

	@Test
	public void inject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MailModulesMock());
		final MailDashboardWidget o = injector.getInstance(MailDashboardWidget.class);
		assertNotNull(o);
	}
}
