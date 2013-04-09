package de.benjaminborbe.mail.gui.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.mail.gui.guice.MailGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class MailGuiDashboardWidgetIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MailGuiModulesMock());
		final MailGuiDashboardWidget mailGuiDashboardWidget = injector.getInstance(MailGuiDashboardWidget.class);
		assertNotNull(mailGuiDashboardWidget);
	}
}
