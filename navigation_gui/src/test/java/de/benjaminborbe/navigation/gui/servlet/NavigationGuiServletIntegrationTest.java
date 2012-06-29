package de.benjaminborbe.navigation.gui.servlet;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.google.inject.Injector;
import de.benjaminborbe.navigation.gui.guice.NavigationGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class NavigationGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new NavigationGuiModulesMock());
		final NavigationGuiServlet a = injector.getInstance(NavigationGuiServlet.class);
		final NavigationGuiServlet b = injector.getInstance(NavigationGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
