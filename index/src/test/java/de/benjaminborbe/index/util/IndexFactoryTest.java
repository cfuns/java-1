package de.benjaminborbe.index.util;

import org.apache.lucene.store.Directory;

import com.google.inject.Injector;

import de.benjaminborbe.index.guice.IndexModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import junit.framework.TestCase;

public class IndexFactoryTest extends TestCase {

	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new IndexModulesMock());
		final IndexFactory a = injector.getInstance(IndexFactory.class);
		final IndexFactory b = injector.getInstance(IndexFactory.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

	public void testGetIndex() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new IndexModulesMock());
		final IndexFactory indexFactory = injector.getInstance(IndexFactory.class);
		final Directory a1 = indexFactory.getIndex("indexA");
		final Directory a2 = indexFactory.getIndex("indexA");
		final Directory b1 = indexFactory.getIndex("indexB");
		assertEquals(a1, a2);
		assertTrue(a1 == a2);
		assertFalse(a1 == b1);
		assertFalse(a2 == b1);
	}
}
