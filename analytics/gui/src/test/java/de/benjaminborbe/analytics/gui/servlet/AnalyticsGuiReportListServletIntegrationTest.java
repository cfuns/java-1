package de.benjaminborbe.analytics.gui.servlet;

import com.google.inject.Injector;
import de.benjaminborbe.analytics.gui.guice.AnalyticsGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AnalyticsGuiReportListServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new AnalyticsGuiModulesMock());
		final AnalyticsGuiReportListServlet a = injector.getInstance(AnalyticsGuiReportListServlet.class);
		final AnalyticsGuiReportListServlet b = injector.getInstance(AnalyticsGuiReportListServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
