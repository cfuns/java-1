package de.benjaminborbe.gallery.image;

import com.google.common.base.Predicate;

import de.benjaminborbe.gallery.api.GalleryIdentifier;

public class GalleryPredicate implements Predicate<GalleryImageBean> {

	private final GalleryIdentifier galleryIdentifier;

	public GalleryPredicate(final GalleryIdentifier galleryIdentifier) {
		this.galleryIdentifier = galleryIdentifier;
	}

	@Override
	public boolean apply(final GalleryImageBean galleryImage) {
		return galleryImage.getGalleryIdentifier().equals(galleryIdentifier);
	}

}
