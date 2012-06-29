package de.benjaminborbe.gwt.gui.servlet;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.google.inject.Injector;
import de.benjaminborbe.gwt.gui.guice.GwtGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class GwtGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new GwtGuiModulesMock());
		final GwtGuiServlet a = injector.getInstance(GwtGuiServlet.class);
		final GwtGuiServlet b = injector.getInstance(GwtGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
