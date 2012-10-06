package de.benjaminborbe.gallery.gallery;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.gallery.api.GalleryIdentifier;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;

@Singleton
public class GalleryDaoStorage extends DaoStorage<GalleryBean, GalleryIdentifier> implements GalleryDao {

	@Inject
	public GalleryDaoStorage(
			final Logger logger,
			final StorageService storageService,
			final Provider<GalleryBean> beanProvider,
			final GalleryBeanMapper mapper,
			final GalleryIdentifierBuilder identifierBuilder) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder);
	}

	private static final String COLUMN_FAMILY = "gallery";

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

}
