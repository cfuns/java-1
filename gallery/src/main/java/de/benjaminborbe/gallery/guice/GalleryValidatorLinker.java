package de.benjaminborbe.gallery.guice;

import com.google.inject.Inject;

import de.benjaminborbe.gallery.validation.GalleryCollectionValidator;
import de.benjaminborbe.gallery.validation.GalleryEntryValidator;
import de.benjaminborbe.gallery.validation.GalleryGroupValidator;
import de.benjaminborbe.tools.validation.ValidatorRegistry;

public class GalleryValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final GalleryGroupValidator galleryGroupValidator,
			final GalleryCollectionValidator galleryCollectionValidator, final GalleryEntryValidator galleryEntryValidator) {
		validatorRegistry.register(galleryGroupValidator);
		validatorRegistry.register(galleryCollectionValidator);
		validatorRegistry.register(galleryEntryValidator);
	}
}
