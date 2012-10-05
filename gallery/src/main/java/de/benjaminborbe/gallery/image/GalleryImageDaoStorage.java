package de.benjaminborbe.gallery.image;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.gallery.api.GalleryImageIdentifier;
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

}
