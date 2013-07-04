package de.benjaminborbe.storage.util;

import com.google.inject.Injector;
import de.benjaminborbe.storage.config.StorageConfig;
import de.benjaminborbe.storage.guice.StorageModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StorageConfigIntegrationTest {

	@Test
	public void testKeySpace() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());
		final StorageConfig config = injector.getInstance(StorageConfig.class);
		assertNotNull(config);
		assertEquals("test", config.getKeySpace());
	}

}
