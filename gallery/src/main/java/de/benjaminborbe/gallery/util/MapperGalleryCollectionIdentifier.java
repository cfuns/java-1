package de.benjaminborbe.gallery.util;

import de.benjaminborbe.gallery.api.GalleryCollectionIdentifier;
import de.benjaminborbe.tools.mapper.Mapper;

public class MapperGalleryCollectionIdentifier implements Mapper<GalleryCollectionIdentifier> {

	@Override
	public String toString(final GalleryCollectionIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public GalleryCollectionIdentifier fromString(final String value) {
		return value != null ? new GalleryCollectionIdentifier(value) : null;
	}

}
