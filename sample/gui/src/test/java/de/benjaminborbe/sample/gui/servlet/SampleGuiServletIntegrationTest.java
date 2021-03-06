package de.benjaminborbe.sample.gui.servlet;

import com.google.inject.Injector;
import de.benjaminborbe.sample.gui.guice.SampleGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SampleGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SampleGuiModulesMock());
		final SampleGuiServlet a = injector.getInstance(SampleGuiServlet.class);
		final SampleGuiServlet b = injector.getInstance(SampleGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
