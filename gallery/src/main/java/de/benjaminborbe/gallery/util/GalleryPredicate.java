package de.benjaminborbe.gallery.util;

import com.google.common.base.Predicate;

import de.benjaminborbe.gallery.api.GalleryCollectionIdentifier;
import de.benjaminborbe.gallery.dao.GalleryEntryBean;

public class GalleryPredicate implements Predicate<GalleryEntryBean> {

	private final GalleryCollectionIdentifier galleryCollectionIdentifier;

	public GalleryPredicate(final GalleryCollectionIdentifier galleryCollectionIdentifier) {
		this.galleryCollectionIdentifier = galleryCollectionIdentifier;
	}

	@Override
	public boolean apply(final GalleryEntryBean galleryEntryBean) {
		return galleryEntryBean != null && galleryEntryBean.getCollectionId() != null && galleryEntryBean.getCollectionId().equals(galleryCollectionIdentifier);
	}
}
