package de.benjaminborbe.gallery.image;

import com.google.common.base.Predicate;

import de.benjaminborbe.gallery.api.GalleryIdentifier;
import de.benjaminborbe.gallery.api.GalleryImageIdentifier;

public class GalleryIdentifierPredicate implements Predicate<GalleryImageIdentifier> {

	private final GalleryIdentifier galleryIdentifier;

	public GalleryIdentifierPredicate(final GalleryIdentifier galleryIdentifier) {
		this.galleryIdentifier = galleryIdentifier;
	}

	@Override
	public boolean apply(final GalleryImageIdentifier galleryImageIdentifier) {
		return galleryImageIdentifier.equals(galleryIdentifier);
	}

}
