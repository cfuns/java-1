package de.benjaminborbe.gallery.api;

import de.benjaminborbe.api.IdentifierBase;

public class GalleryEntryIdentifier extends IdentifierBase<String> {

	public GalleryEntryIdentifier(final long id) {
		this(String.valueOf(id));
	}

	public GalleryEntryIdentifier(final String id) {
		super(id);
	}

}
