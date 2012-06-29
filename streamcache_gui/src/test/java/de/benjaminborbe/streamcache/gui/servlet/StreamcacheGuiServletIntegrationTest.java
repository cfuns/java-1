package de.benjaminborbe.streamcache.gui.servlet;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.google.inject.Injector;
import de.benjaminborbe.streamcache.gui.guice.StreamcacheGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class StreamcacheGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StreamcacheGuiModulesMock());
		final StreamcacheGuiServlet a = injector.getInstance(StreamcacheGuiServlet.class);
		final StreamcacheGuiServlet b = injector.getInstance(StreamcacheGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
