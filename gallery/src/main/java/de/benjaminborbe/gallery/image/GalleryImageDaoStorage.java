package de.benjaminborbe.gallery.image;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;

import com.google.common.base.Predicate;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.gallery.api.GalleryIdentifier;
import de.benjaminborbe.gallery.api.GalleryImageIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;

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
		try {
			logger.debug("getGalleryImages id: " + galleryIdentifier);
			final Predicate<GalleryImageBean> p = new GalleryPredicate(galleryIdentifier);
			final EntityIterator<GalleryImageBean> i = getEntityIterator();
			final List<GalleryImageBean> result = new ArrayList<GalleryImageBean>();
			while (i.hasNext()) {
				final GalleryImageBean image = i.next();
				if (p.apply(image)) {
					result.add(image);
				}
			}
			return result;
		}
		catch (final EntityIteratorException e) {
			throw new StorageException(e);
		}
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

}
