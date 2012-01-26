package de.benjaminborbe.bookmark.gui.guice;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.bookmark.api.BookmarkService;
import de.benjaminborbe.bookmark.api.BookmarkServiceMock;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.navigation.service.NavigationWidgetMock;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.osgi.mock.LogServiceMock;

public class BookmarkGuiOsgiModuleMock extends AbstractModule {

	@Override
	protected void configure() {
		bind(BookmarkService.class).to(BookmarkServiceMock.class).in(Singleton.class);
		bind(NavigationWidget.class).to(NavigationWidgetMock.class).in(Singleton.class);
		bind(LogService.class).to(LogServiceMock.class).in(Singleton.class);
		bind(ExtHttpService.class).to(ExtHttpServiceMock.class).in(Singleton.class);
	}
}
