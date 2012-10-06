package de.benjaminborbe.gallery.image;

import java.util.Collection;

import de.benjaminborbe.gallery.api.GalleryIdentifier;
import de.benjaminborbe.gallery.api.GalleryImageIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;

public interface GalleryImageDao extends Dao<GalleryImageBean, GalleryImageIdentifier> {

	Collection<GalleryImageBean> getGalleryImages(GalleryIdentifier galleryIdentifier) throws StorageException;

	Collection<GalleryImageIdentifier> getGalleryImageIdentifiers(GalleryIdentifier galleryIdentifier) throws StorageException;

}
