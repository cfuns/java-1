package de.benjaminborbe.gallery.api;

import de.benjaminborbe.api.IdentifierBase;

public class GalleryImageIdentifier extends IdentifierBase<String> {

	public GalleryImageIdentifier(final long id) {
		this(String.valueOf(id));
	}

	public GalleryImageIdentifier(final String id) {
		super(id);
	}

}
