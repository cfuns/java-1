package de.benjaminborbe.translate.gui.servlet;

import com.google.inject.Injector;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.translate.gui.guice.TranslateGuiModulesMock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TranslateGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TranslateGuiModulesMock());
		final TranslateGuiServlet a = injector.getInstance(TranslateGuiServlet.class);
		final TranslateGuiServlet b = injector.getInstance(TranslateGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
