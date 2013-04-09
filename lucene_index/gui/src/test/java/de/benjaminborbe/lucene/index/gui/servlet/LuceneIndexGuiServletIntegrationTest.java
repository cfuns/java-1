package de.benjaminborbe.lucene.index.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.lucene.index.gui.guice.LuceneIndexGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class LuceneIndexGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new LuceneIndexGuiModulesMock());
		final LuceneIndexGuiServlet a = injector.getInstance(LuceneIndexGuiServlet.class);
		final LuceneIndexGuiServlet b = injector.getInstance(LuceneIndexGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
