package de.benjaminborbe.index.guice;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.bookmark.api.BookmarkService;
import de.benjaminborbe.bookmark.api.BookmarkServiceMock;
import de.benjaminborbe.tools.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.mock.LogServiceMock;

public class IndexOsgiModuleMock extends AbstractModule {

	@Override
	protected void configure() {
		bind(BookmarkService.class).to(BookmarkServiceMock.class).in(Singleton.class);
		bind(LogService.class).to(LogServiceMock.class).in(Singleton.class);
		bind(ExtHttpService.class).to(ExtHttpServiceMock.class).in(Singleton.class);
	}
}
