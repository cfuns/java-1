package de.benjaminborbe.poker.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.poker.gui.guice.PokerGuiModulesMock;
import de.benjaminborbe.poker.gui.servlet.PokerGuiServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class PokerGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PokerGuiModulesMock());
		final PokerGuiServlet a = injector.getInstance(PokerGuiServlet.class);
		final PokerGuiServlet b = injector.getInstance(PokerGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
