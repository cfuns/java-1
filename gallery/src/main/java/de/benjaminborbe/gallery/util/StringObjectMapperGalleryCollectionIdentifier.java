package de.benjaminborbe.gallery.util;

import de.benjaminborbe.gallery.api.GalleryCollectionIdentifier;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperBase;

public class StringObjectMapperGalleryCollectionIdentifier<B> extends StringObjectMapperBase<B, GalleryCollectionIdentifier> {

	public StringObjectMapperGalleryCollectionIdentifier(final String name) {
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
