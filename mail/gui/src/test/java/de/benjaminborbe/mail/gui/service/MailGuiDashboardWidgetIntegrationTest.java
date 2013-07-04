package de.benjaminborbe.mail.gui.service;

import com.google.inject.Injector;
import de.benjaminborbe.mail.gui.guice.MailGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class MailGuiDashboardWidgetIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MailGuiModulesMock());
		final MailGuiDashboardWidget mailGuiDashboardWidget = injector.getInstance(MailGuiDashboardWidget.class);
		assertNotNull(mailGuiDashboardWidget);
	}
}
