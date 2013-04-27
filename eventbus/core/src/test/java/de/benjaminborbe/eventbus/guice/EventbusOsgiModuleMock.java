package de.benjaminborbe.eventbus.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.navigation.mock.NavigationWidgetMock;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.osgi.mock.LogServiceMock;
import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import javax.inject.Singleton;

public class EventbusOsgiModuleMock extends AbstractModule {

	@Override
	protected void configure() {
		bind(NavigationWidget.class).to(NavigationWidgetMock.class).in(Singleton.class);
		bind(LogService.class).to(LogServiceMock.class).in(Singleton.class);
		bind(ExtHttpService.class).to(ExtHttpServiceMock.class).in(Singleton.class);
	}
}
