package de.benjaminborbe.gallery.dao;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.gallery.api.GalleryGroupIdentifier;

public class GalleryGroupIdentifierBuilder implements IdentifierBuilder<String, GalleryGroupIdentifier> {

	@Override
	public GalleryGroupIdentifier buildIdentifier(final String value) {
		return new GalleryGroupIdentifier(value);
	}

}
