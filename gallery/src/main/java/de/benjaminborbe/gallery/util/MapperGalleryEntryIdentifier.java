package de.benjaminborbe.gallery.util;

import de.benjaminborbe.gallery.api.GalleryEntryIdentifier;
import de.benjaminborbe.tools.mapper.Mapper;

public class MapperGalleryEntryIdentifier implements Mapper<GalleryEntryIdentifier> {

	@Override
	public String toString(final GalleryEntryIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public GalleryEntryIdentifier fromString(final String value) {
		return value != null ? new GalleryEntryIdentifier(value) : null;
	}

}
