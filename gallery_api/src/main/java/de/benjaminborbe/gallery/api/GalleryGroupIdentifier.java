package de.benjaminborbe.gallery.api;

import de.benjaminborbe.api.IdentifierBase;

public class GalleryGroupIdentifier extends IdentifierBase<String> {

	public GalleryGroupIdentifier(final long id) {
		this(String.valueOf(id));
	}

	public GalleryGroupIdentifier(final String id) {
		super(id);
	}

}
