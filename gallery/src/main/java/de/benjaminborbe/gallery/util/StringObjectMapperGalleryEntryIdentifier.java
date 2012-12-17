package de.benjaminborbe.gallery.util;

import de.benjaminborbe.gallery.api.GalleryEntryIdentifier;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperBase;

public class StringObjectMapperGalleryEntryIdentifier<B> extends StringObjectMapperBase<B, GalleryEntryIdentifier> {

	public StringObjectMapperGalleryEntryIdentifier(final String name) {
		super(name);
	}

	@Override
	public String toString(final GalleryEntryIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public GalleryEntryIdentifier fromString(final String value) {
		return value != null ? new GalleryEntryIdentifier(value) : null;
	}

}
