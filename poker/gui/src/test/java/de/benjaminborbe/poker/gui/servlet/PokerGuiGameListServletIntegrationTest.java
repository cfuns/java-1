package de.benjaminborbe.poker.gui.servlet;

import com.google.inject.Injector;
import de.benjaminborbe.poker.gui.guice.PokerGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PokerGuiGameListServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PokerGuiModulesMock());
		final PokerGuiGameListServlet a = injector.getInstance(PokerGuiGameListServlet.class);
		final PokerGuiGameListServlet b = injector.getInstance(PokerGuiGameListServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
