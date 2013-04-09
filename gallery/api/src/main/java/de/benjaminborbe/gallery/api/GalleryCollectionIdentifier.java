package de.benjaminborbe.gallery.api;

import de.benjaminborbe.api.IdentifierBase;

public class GalleryCollectionIdentifier extends IdentifierBase<String> {

	public GalleryCollectionIdentifier(final long id) {
		this(String.valueOf(id));
	}

	public GalleryCollectionIdentifier(final String id) {
		super(id);
	}

}
