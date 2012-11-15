package de.benjaminborbe.gallery.util;

import com.google.common.base.Predicate;

import de.benjaminborbe.gallery.api.GalleryCollectionIdentifier;
import de.benjaminborbe.gallery.dao.GalleryEntryBean;

public class GalleryPredicate implements Predicate<GalleryEntryBean> {

	private final GalleryCollectionIdentifier galleryIdentifier;

	public GalleryPredicate(final GalleryCollectionIdentifier galleryIdentifier) {
		this.galleryIdentifier = galleryIdentifier;
	}

	@Override
	public boolean apply(final GalleryEntryBean galleryImage) {
		return galleryImage.getGalleryIdentifier().equals(galleryIdentifier);
	}

}
