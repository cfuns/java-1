package de.benjaminborbe.gallery.util;

import de.benjaminborbe.gallery.api.GalleryEntryIdentifier;
import de.benjaminborbe.tools.mapper.SingleMapBase;

public class SingleMapGalleryEntryIdentifier<B> extends SingleMapBase<B, GalleryEntryIdentifier> {

	public SingleMapGalleryEntryIdentifier(final String name) {
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
