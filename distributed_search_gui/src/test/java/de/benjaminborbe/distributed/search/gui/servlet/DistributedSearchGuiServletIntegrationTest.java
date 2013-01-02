package de.benjaminborbe.distributed.search.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.distributed.search.gui.guice.DistributedSearchGuiModulesMock;
import de.benjaminborbe.distributed.search.gui.servlet.DistributedSearchGuiServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class DistributedSearchGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new DistributedSearchGuiModulesMock());
		final DistributedSearchGuiServlet a = injector.getInstance(DistributedSearchGuiServlet.class);
		final DistributedSearchGuiServlet b = injector.getInstance(DistributedSearchGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
