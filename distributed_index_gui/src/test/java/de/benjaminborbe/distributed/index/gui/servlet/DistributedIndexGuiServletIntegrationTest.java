package de.benjaminborbe.distributed.index.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.distributed.index.gui.guice.DistributedIndexGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class DistributedIndexGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new DistributedIndexGuiModulesMock());
		final DistributedIndexGuiServlet a = injector.getInstance(DistributedIndexGuiServlet.class);
		final DistributedIndexGuiServlet b = injector.getInstance(DistributedIndexGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
