package de.benjaminborbe.gallery.util;

import de.benjaminborbe.gallery.api.GalleryImageIdentifier;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperBase;

public class StringObjectMapperGalleryImageIdentifier<B> extends StringObjectMapperBase<B, GalleryImageIdentifier> {

	public StringObjectMapperGalleryImageIdentifier(final String name) {
		super(name);
	}

	@Override
	public String toString(final GalleryImageIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public GalleryImageIdentifier fromString(final String value) {
		return value != null ? new GalleryImageIdentifier(value) : null;
	}

}
