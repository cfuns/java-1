package de.benjaminborbe.kiosk.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.kiosk.gui.guice.KioskGuiModulesMock;
import de.benjaminborbe.kiosk.gui.servlet.KioskGuiServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class KioskGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new KioskGuiModulesMock());
		final KioskGuiServlet a = injector.getInstance(KioskGuiServlet.class);
		final KioskGuiServlet b = injector.getInstance(KioskGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
