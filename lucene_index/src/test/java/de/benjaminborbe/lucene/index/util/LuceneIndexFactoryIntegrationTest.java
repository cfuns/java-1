package de.benjaminborbe.lucene.index.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.lucene.store.Directory;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.lucene.index.util.LuceneIndexFactory;
import de.benjaminborbe.lucene.index.guice.LuceneIndexModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class LuceneIndexFactoryIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new LuceneIndexModulesMock());
		final LuceneIndexFactory a = injector.getInstance(LuceneIndexFactory.class);
		final LuceneIndexFactory b = injector.getInstance(LuceneIndexFactory.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

	@Test
	public void testGetLuceneIndex() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new LuceneIndexModulesMock());
		final LuceneIndexFactory indexFactory = injector.getInstance(LuceneIndexFactory.class);
		final Directory a1 = indexFactory.getLuceneIndex("indexA");
		final Directory a2 = indexFactory.getLuceneIndex("indexA");
		final Directory b1 = indexFactory.getLuceneIndex("indexB");
		assertEquals(a1, a2);
		assertTrue(a1 == a2);
		assertFalse(a1 == b1);
		assertFalse(a2 == b1);
	}
}
