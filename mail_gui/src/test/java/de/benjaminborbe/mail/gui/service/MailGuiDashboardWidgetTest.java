package de.benjaminborbe.mail.gui.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.mail.gui.guice.MailGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class MailGuiDashboardWidgetTest {

	@Test
	public void inject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MailGuiModulesMock());
		final MailGuiDashboardWidget o = injector.getInstance(MailGuiDashboardWidget.class);
		assertNotNull(o);
	}
}
