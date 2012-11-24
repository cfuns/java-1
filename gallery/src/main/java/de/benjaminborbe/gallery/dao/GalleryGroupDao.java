package de.benjaminborbe.gallery.dao;

import de.benjaminborbe.gallery.api.GalleryGroupIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;
import de.benjaminborbe.storage.tools.EntityIterator;

public interface GalleryGroupDao extends Dao<GalleryGroupBean, GalleryGroupIdentifier> {

	EntityIterator<GalleryGroupBean> getEntityIteratorPublic() throws StorageException;

}
