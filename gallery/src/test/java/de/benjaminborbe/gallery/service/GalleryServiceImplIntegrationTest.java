package de.benjaminborbe.gallery.service;

import static org.junit.Assert.*;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.gallery.guice.GalleryModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class GalleryServiceImplIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new GalleryModulesMock());
		final GalleryService galleryService = injector.getInstance(GalleryService.class);
		assertNotNull(galleryService);
		assertEquals(GalleryServiceImpl.class, galleryService.getClass());
	}

	@Test
	public void testValidation() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new GalleryModulesMock());
		final GalleryService galleryService = injector.getInstance(GalleryService.class);

		try {
			final String groupName = null;
			final SessionIdentifier sessionIdentifier = null;
			galleryService.createGroup(sessionIdentifier, groupName);
			fail("ValidationException expected");
		}
		catch (final ValidationException e) {
			assertNotNull(e);
		}

		{
			final String groupName = "testGroup";
			final SessionIdentifier sessionIdentifier = null;
			galleryService.createGroup(sessionIdentifier, groupName);
		}
	}

}
