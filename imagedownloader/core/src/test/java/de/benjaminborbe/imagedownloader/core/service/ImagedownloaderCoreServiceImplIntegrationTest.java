package de.benjaminborbe.imagedownloader.core.service;

import com.google.inject.Injector;
import de.benjaminborbe.imagedownloader.core.guice.ImagedownloaderModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ImagedownloaderCoreServiceImplIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ImagedownloaderModulesMock());
		assertNotNull(injector.getInstance(ImagedownloaderCoreServiceImpl.class));
	}
}
