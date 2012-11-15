package de.benjaminborbe.gallery.dao;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.gallery.api.GalleryCollectionIdentifier;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;

@Singleton
public class GalleryCollectionDaoStorage extends DaoStorage<GalleryCollectionBean, GalleryCollectionIdentifier> implements GalleryCollectionDao {

	@Inject
	public GalleryCollectionDaoStorage(
			final Logger logger,
			final StorageService storageService,
			final Provider<GalleryCollectionBean> beanProvider,
			final GalleryCollectionBeanMapper mapper,
			final GalleryCollectionIdentifierBuilder identifierBuilder) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder);
	}

	private static final String COLUMN_FAMILY = "gallery_collection";

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

}
