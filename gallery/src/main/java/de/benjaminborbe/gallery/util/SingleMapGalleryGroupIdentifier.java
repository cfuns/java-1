package de.benjaminborbe.gallery.util;

import de.benjaminborbe.gallery.api.GalleryGroupIdentifier;
import de.benjaminborbe.tools.mapper.SingleMapBase;

public class SingleMapGalleryGroupIdentifier<B> extends SingleMapBase<B, GalleryGroupIdentifier> {

	public SingleMapGalleryGroupIdentifier(final String name) {
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
