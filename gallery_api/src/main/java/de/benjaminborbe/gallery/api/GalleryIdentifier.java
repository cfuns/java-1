package de.benjaminborbe.gallery.api;

import de.benjaminborbe.api.IdentifierBase;

public class GalleryIdentifier extends IdentifierBase<String> {

	public GalleryIdentifier(final long id) {
		this(String.valueOf(id));
	}

	public GalleryIdentifier(final String id) {
		super(id);
	}

}
