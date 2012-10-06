package de.benjaminborbe.gallery.image;

import java.util.Collection;

import org.slf4j.Logger;

import com.google.common.collect.Collections2;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.gallery.api.GalleryIdentifier;
import de.benjaminborbe.gallery.api.GalleryImageIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;

@Singleton
public class GalleryImageDaoStorage extends DaoStorage<GalleryImageBean, GalleryImageIdentifier> implements GalleryImageDao {

	private static final String COLUMN_FAMILY = "gallery_image";

	@Inject
	public GalleryImageDaoStorage(
			final Logger logger,
			final StorageService storageService,
			final Provider<GalleryImageBean> beanProvider,
			final GalleryImageBeanMapper mapper,
			final GalleryImageIdentifierBuilder identifierBuilder) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder);
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

	@Override
	public Collection<GalleryImageBean> getGalleryImages(final GalleryIdentifier galleryIdentifier) throws StorageException {
		final Collection<GalleryImageBean> images = getAll();
		return Collections2.filter(images, new GalleryPredicate(galleryIdentifier));
	}

	@Override
	public Collection<GalleryImageIdentifier> getGalleryImageIdentifiers(final GalleryIdentifier galleryIdentifier) throws StorageException {
		final Collection<GalleryImageIdentifier> images = getIdentifiers();
		return Collections2.filter(images, new GalleryIdentifierPredicate(galleryIdentifier));
	}

}
