package de.benjaminborbe.meta.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.meta.util.BundleResolver;
import de.benjaminborbe.meta.util.BundleResolverImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.url.UrlUtilImpl;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class MetaModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(UrlUtil.class).to(UrlUtilImpl.class);
		bind(BundleResolver.class).to(BundleResolverImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
