package de.benjaminborbe.gallery.dao;

import de.benjaminborbe.gallery.api.GalleryCollectionIdentifier;
import de.benjaminborbe.gallery.api.GalleryEntryIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;
import de.benjaminborbe.storage.tools.EntityIterator;

import java.util.Collection;

public interface GalleryEntryDao extends Dao<GalleryEntryBean, GalleryEntryIdentifier> {

	Collection<GalleryEntryBean> getGalleryImages(GalleryCollectionIdentifier galleryIdentifier) throws StorageException;

	Collection<GalleryEntryIdentifier> getGalleryImageIdentifiers(GalleryCollectionIdentifier galleryIdentifier) throws StorageException;

	EntityIterator<GalleryEntryBean> getEntityIteratorShared() throws StorageException;

}
