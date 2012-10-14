package de.benjaminborbe.vnc.guice;

import org.slf4j.Logger;

import com.glavsoft.viewer.Viewer;
import com.glavsoft.viewer.ViewerHeadless;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import de.benjaminborbe.vnc.api.VncScreenContent;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.service.VncServiceImpl;
import de.benjaminborbe.vnc.util.VncScreenContentImpl;

public class VncModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Viewer.class).to(ViewerHeadless.class);
		bind(VncScreenContent.class).to(VncScreenContentImpl.class).in(Singleton.class);
		bind(VncService.class).to(VncServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
