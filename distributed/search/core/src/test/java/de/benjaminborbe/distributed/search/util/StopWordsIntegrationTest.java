package de.benjaminborbe.distributed.search.util;

import com.google.inject.Injector;
import de.benjaminborbe.distributed.search.guice.DistributedSearchModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StopWordsIntegrationTest {

	@Test
	public void testSingleton() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new DistributedSearchModulesMock());
		final StopWords stopWordsA = injector.getInstance(StopWords.class);
		assertNotNull(stopWordsA);
		final StopWords stopWordsB = injector.getInstance(StopWords.class);
		assertNotNull(stopWordsB);
		assertEquals(stopWordsA, stopWordsB);
	}
}
