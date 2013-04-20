package de.benjaminborbe.gallery.dao;

import org.slf4j.Logger;

import javax.inject.Inject;
import com.google.inject.Provider;
import javax.inject.Singleton;

import de.benjaminborbe.gallery.api.GalleryImageIdentifier;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.tools.date.CalendarUtil;

@Singleton
public class GalleryImageDaoStorage extends DaoStorage<GalleryImageBean, GalleryImageIdentifier> implements GalleryImageDao {

	private static final String COLUMN_FAMILY = "gallery_image";

	@Inject
	public GalleryImageDaoStorage(
			final Logger logger,
			final StorageService storageService,
			final Provider<GalleryImageBean> beanProvider,
			final GalleryImageBeanMapper mapper,
			final GalleryImageIdentifierBuilder identifierBuilder,
			final CalendarUtil calendarUtil) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

}
