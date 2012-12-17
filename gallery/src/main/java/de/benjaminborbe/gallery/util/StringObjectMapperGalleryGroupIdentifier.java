package de.benjaminborbe.gallery.util;

import de.benjaminborbe.gallery.api.GalleryGroupIdentifier;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperBase;

public class StringObjectMapperGalleryGroupIdentifier<B> extends StringObjectMapperBase<B, GalleryGroupIdentifier> {

	public StringObjectMapperGalleryGroupIdentifier(final String name) {
		super(name);
	}

	@Override
	public String toString(final GalleryGroupIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public GalleryGroupIdentifier fromString(final String value) {
		return value != null ? new GalleryGroupIdentifier(value) : null;
	}

}
