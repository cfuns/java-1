package de.benjaminborbe.storage.api;

import junit.framework.TestCase;

import com.google.inject.Injector;

import de.benjaminborbe.storage.guice.StorageModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class PersistentStorageServiceIntegrationTest extends TestCase {

	public void testIsSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());
		final PersistentStorageService o1 = injector.getInstance(PersistentStorageService.class);
		final PersistentStorageService o2 = injector.getInstance(PersistentStorageService.class);
		assertEquals(o1, o2);
		assertTrue(o1 == o2);
	}

}
