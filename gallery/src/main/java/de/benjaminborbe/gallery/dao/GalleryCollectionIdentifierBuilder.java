package de.benjaminborbe.gallery.dao;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.gallery.api.GalleryCollectionIdentifier;

public class GalleryCollectionIdentifierBuilder implements IdentifierBuilder<String, GalleryCollectionIdentifier> {

	@Override
	public GalleryCollectionIdentifier buildIdentifier(final String value) {
		return new GalleryCollectionIdentifier(value);
	}

}
