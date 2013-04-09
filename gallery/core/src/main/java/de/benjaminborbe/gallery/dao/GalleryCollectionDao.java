package de.benjaminborbe.gallery.dao;

import de.benjaminborbe.gallery.api.GalleryCollectionIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;
import de.benjaminborbe.storage.tools.EntityIterator;

public interface GalleryCollectionDao extends Dao<GalleryCollectionBean, GalleryCollectionIdentifier> {

	EntityIterator<GalleryCollectionBean> getEntityIteratorShared() throws StorageException;

}
