package de.benjaminborbe.gallery.dao;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.gallery.api.GalleryEntryIdentifier;

public class GalleryEntryIdentifierBuilder implements IdentifierBuilder<String, GalleryEntryIdentifier> {

	@Override
	public GalleryEntryIdentifier buildIdentifier(final String value) {
		return new GalleryEntryIdentifier(value);
	}

}
