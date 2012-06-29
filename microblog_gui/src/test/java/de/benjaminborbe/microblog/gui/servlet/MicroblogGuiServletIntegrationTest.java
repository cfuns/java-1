package de.benjaminborbe.microblog.gui.servlet;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.google.inject.Injector;
import de.benjaminborbe.microblog.gui.guice.MicroblogGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class MicroblogGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MicroblogGuiModulesMock());
		final MicroblogGuiServlet a = injector.getInstance(MicroblogGuiServlet.class);
		final MicroblogGuiServlet b = injector.getInstance(MicroblogGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
