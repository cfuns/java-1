package de.benjaminborbe.cms.gui.servlet;

import com.google.inject.Injector;
import de.benjaminborbe.cms.gui.guice.CmsGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CmsGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new CmsGuiModulesMock());
		final CmsGuiServlet a = injector.getInstance(CmsGuiServlet.class);
		final CmsGuiServlet b = injector.getInstance(CmsGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
