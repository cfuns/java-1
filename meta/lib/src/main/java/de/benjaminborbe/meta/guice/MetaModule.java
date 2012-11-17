package de.benjaminborbe.meta.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.meta.util.BundleResolver;
import de.benjaminborbe.meta.util.BundleResolverImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class MetaModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(BundleResolver.class).to(BundleResolverImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
