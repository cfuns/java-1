package de.benjaminborbe.gallery.gallery;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.gallery.api.GalleryIdentifier;

public class GalleryIdentifierBuilder implements IdentifierBuilder<String, GalleryIdentifier> {

	@Override
	public GalleryIdentifier buildIdentifier(final String value) {
		return new GalleryIdentifier(value);
	}

}
