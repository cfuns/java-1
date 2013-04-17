package de.benjaminborbe.gallery.dao;

import com.google.common.base.Predicate;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import de.benjaminborbe.gallery.api.GalleryCollectionIdentifier;
import de.benjaminborbe.gallery.api.GalleryEntryIdentifier;
import de.benjaminborbe.gallery.util.GalleryPredicate;
import de.benjaminborbe.gallery.util.SharedPredicate;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.storage.tools.EntityIteratorFilter;
import de.benjaminborbe.tools.date.CalendarUtil;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Singleton
public class GalleryEntryDaoStorage extends DaoStorage<GalleryEntryBean, GalleryEntryIdentifier> implements GalleryEntryDao {

	private static final String COLUMN_FAMILY = "gallery_entry";

	private final Logger logger;

	@Inject
	public GalleryEntryDaoStorage(
		final Logger logger,
		final StorageService storageService,
		final Provider<GalleryEntryBean> beanProvider,
		final GalleryEntryBeanMapper mapper,
		final GalleryEntryIdentifierBuilder identifierBuilder,
		final CalendarUtil calendarUtil) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
		this.logger = logger;
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

	@Override
	public Collection<GalleryEntryBean> getGalleryImages(final GalleryCollectionIdentifier galleryIdentifier) throws StorageException {
		try {
			logger.debug("getGalleryImages id: " + galleryIdentifier);
			final Predicate<GalleryEntryBean> p = new GalleryPredicate(galleryIdentifier);
			final EntityIterator<GalleryEntryBean> i = getEntityIterator();
			final List<GalleryEntryBean> result = new ArrayList<>();
			while (i.hasNext()) {
				final GalleryEntryBean image = i.next();
				if (p.apply(image)) {
					result.add(image);
				}
			}
			return result;
		} catch (final EntityIteratorException e) {
			throw new StorageException(e);
		}
	}

	@Override
	public Collection<GalleryEntryIdentifier> getGalleryImageIdentifiers(final GalleryCollectionIdentifier galleryIdentifier) throws StorageException {
		final Collection<GalleryEntryBean> images = getGalleryImages(galleryIdentifier);
		final Set<GalleryEntryIdentifier> result = new HashSet<>();
		for (final GalleryEntryBean image : images) {
			result.add(image.getId());
		}
		return result;
	}

	@Override
	public EntityIterator<GalleryEntryBean> getEntityIteratorShared() throws StorageException {
		return new EntityIteratorFilter<>(getEntityIterator(), new SharedPredicate<GalleryEntryBean>());
	}

}
