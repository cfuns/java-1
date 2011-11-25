package de.benjaminborbe.storage.api;

import junit.framework.TestCase;

import com.google.inject.Injector;

import de.benjaminborbe.storage.api.CacheStorageService;
import de.benjaminborbe.storage.guice.StorageModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class CacheStorageServiceIntegrationTest extends TestCase {

	public void testIsSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());
		final CacheStorageService o1 = injector.getInstance(CacheStorageService.class);
		final CacheStorageService o2 = injector.getInstance(CacheStorageService.class);
		assertEquals(o1, o2);
		assertTrue(o1 == o2);
	}
}
