package de.benjaminborbe.scala.gui.servlet;

import com.google.inject.Injector;
import de.benjaminborbe.scala.gui.guice.ScalaGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ScalaGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ScalaGuiModulesMock());
		final ScalaGuiServlet a = injector.getInstance(ScalaGuiServlet.class);
		final ScalaGuiServlet b = injector.getInstance(ScalaGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
