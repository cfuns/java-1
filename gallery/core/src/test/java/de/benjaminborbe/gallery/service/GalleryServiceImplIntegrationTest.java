package de.benjaminborbe.gallery.service;

import com.google.inject.Injector;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.gallery.api.GalleryGroupDto;
import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.gallery.guice.GalleryModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

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
			final GalleryGroupDto dto = new GalleryGroupDto();
			dto.setName(groupName);
			dto.setShared(false);
			galleryService.createGroup(sessionIdentifier, dto);
			fail("ValidationException expected");
		} catch (final ValidationException e) {
			assertNotNull(e);
		}

		{
			final String groupName = "testGroup";
			final SessionIdentifier sessionIdentifier = null;
			final GalleryGroupDto dto = new GalleryGroupDto();
			dto.setName(groupName);
			dto.setShared(false);
			galleryService.createGroup(sessionIdentifier, dto);
		}
	}

}
