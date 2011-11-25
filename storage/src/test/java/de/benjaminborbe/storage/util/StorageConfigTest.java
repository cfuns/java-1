package de.benjaminborbe.storage.util;

import com.google.inject.Injector;

import junit.framework.TestCase;
import de.benjaminborbe.storage.guice.StorageModulesMock;
import de.benjaminborbe.storage.util.StorageConfig;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class StorageConfigTest extends TestCase {

	public void testKeySpace() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());
		final StorageConfig config = injector.getInstance(StorageConfig.class);
		assertNotNull(config);
		assertEquals("bb_test", config.getKeySpace());
	}
}
