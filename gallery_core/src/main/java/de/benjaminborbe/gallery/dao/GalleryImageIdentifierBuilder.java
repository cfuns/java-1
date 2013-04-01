package de.benjaminborbe.gallery.dao;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.gallery.api.GalleryImageIdentifier;

public class GalleryImageIdentifierBuilder implements IdentifierBuilder<String, GalleryImageIdentifier> {

	@Override
	public GalleryImageIdentifier buildIdentifier(final String value) {
		return new GalleryImageIdentifier(value);
	}

}
