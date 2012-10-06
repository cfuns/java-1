package de.benjaminborbe.gallery.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.gallery.gallery.GalleryDao;
import de.benjaminborbe.gallery.gallery.GalleryDaoStorage;
import de.benjaminborbe.gallery.image.GalleryImageDao;
import de.benjaminborbe.gallery.image.GalleryImageDaoStorage;
import de.benjaminborbe.gallery.service.GalleryServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class GalleryModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(GalleryService.class).to(GalleryServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
		bind(GalleryImageDao.class).to(GalleryImageDaoStorage.class).in(Singleton.class);
		bind(GalleryDao.class).to(GalleryDaoStorage.class).in(Singleton.class);
	}
}
