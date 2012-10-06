package de.benjaminborbe.gallery.image;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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

	private final Logger logger;

	@Inject
	public GalleryImageDaoStorage(
			final Logger logger,
			final StorageService storageService,
			final Provider<GalleryImageBean> beanProvider,
			final GalleryImageBeanMapper mapper,
			final GalleryImageIdentifierBuilder identifierBuilder) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder);
		this.logger = logger;
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
		final Collection<GalleryImageBean> images = getGalleryImages(galleryIdentifier);
		final Set<GalleryImageIdentifier> result = new HashSet<GalleryImageIdentifier>();
		for (final GalleryImageBean image : images) {
			result.add(image.getId());
		}
		return result;
	}

	@Override
	public Collection<GalleryImageIdentifier> getIdentifiers() throws StorageException {
		final Collection<GalleryImageIdentifier> result = super.getIdentifiers();
		logger.debug("getIdentifiers => " + result.size());
		return result;
	}

	@Override
	public Collection<GalleryImageBean> getAll() throws StorageException {
		final Collection<GalleryImageBean> result = super.getAll();
		logger.debug("getAll => " + result.size());
		return result;
	}

}
