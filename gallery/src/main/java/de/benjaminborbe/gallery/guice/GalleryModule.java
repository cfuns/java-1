package de.benjaminborbe.gallery.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.gallery.dao.GalleryCollectionDao;
import de.benjaminborbe.gallery.dao.GalleryCollectionDaoStorage;
import de.benjaminborbe.gallery.dao.GalleryEntryDao;
import de.benjaminborbe.gallery.dao.GalleryEntryDaoStorage;
import de.benjaminborbe.gallery.service.GalleryServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class GalleryModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(GalleryService.class).to(GalleryServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
		bind(GalleryEntryDao.class).to(GalleryEntryDaoStorage.class).in(Singleton.class);
		bind(GalleryCollectionDao.class).to(GalleryCollectionDaoStorage.class).in(Singleton.class);
	}
}
