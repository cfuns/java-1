package de.benjaminborbe.microblog.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.microblog.gui.guice.MicroblogGuiModulesMock;
import de.benjaminborbe.microblog.gui.service.MicroblogGuiDashboardWidget;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class MicroblogGuiDashboardWidgetTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MicroblogGuiModulesMock());
		final MicroblogGuiDashboardWidget a = injector.getInstance(MicroblogGuiDashboardWidget.class);
		final MicroblogGuiDashboardWidget b = injector.getInstance(MicroblogGuiDashboardWidget.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
