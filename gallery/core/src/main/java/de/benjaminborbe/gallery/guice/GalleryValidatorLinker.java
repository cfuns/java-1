package de.benjaminborbe.gallery.guice;

import de.benjaminborbe.gallery.validation.GalleryCollectionValidator;
import de.benjaminborbe.gallery.validation.GalleryEntryValidator;
import de.benjaminborbe.gallery.validation.GalleryGroupValidator;
import de.benjaminborbe.gallery.validation.GalleryImageValidator;
import de.benjaminborbe.tools.validation.ValidatorRegistry;

import javax.inject.Inject;

public class GalleryValidatorLinker {

	@Inject
	public static void link(
		final ValidatorRegistry validatorRegistry, final GalleryGroupValidator galleryGroupValidator, final GalleryImageValidator galleryImageValidator,
		final GalleryCollectionValidator galleryCollectionValidator, final GalleryEntryValidator galleryEntryValidator
	) {
		validatorRegistry.register(galleryGroupValidator);
		validatorRegistry.register(galleryCollectionValidator);
		validatorRegistry.register(galleryImageValidator);
		validatorRegistry.register(galleryEntryValidator);
	}
}
