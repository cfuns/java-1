package de.benjaminborbe.gallery.util;

import de.benjaminborbe.gallery.api.GalleryCollectionIdentifier;
import de.benjaminborbe.tools.mapper.SingleMapBase;

public class SingleMapGalleryCollectionIdentifier<B> extends SingleMapBase<B, GalleryCollectionIdentifier> {

	public SingleMapGalleryCollectionIdentifier(final String name) {
		super(name);
	}

	@Override
	public String toString(final GalleryCollectionIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public GalleryCollectionIdentifier fromString(final String value) {
		return value != null ? new GalleryCollectionIdentifier(value) : null;
	}

}
