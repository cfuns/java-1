package de.benjaminborbe.gallery.util;

import de.benjaminborbe.gallery.api.GalleryImageIdentifier;
import de.benjaminborbe.tools.mapper.Mapper;

public class MapperGalleryImageIdentifier implements Mapper<GalleryImageIdentifier> {

	@Override
	public String toString(final GalleryImageIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public GalleryImageIdentifier fromString(final String value) {
		return value != null ? new GalleryImageIdentifier(value) : null;
	}

}
