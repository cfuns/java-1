package de.benjaminborbe.microblog.service;

import org.osgi.framework.BundleContext;

import javax.inject.Singleton;

import de.benjaminborbe.microblog.api.MicroblogPostListener;
import de.benjaminborbe.microblog.util.MicroblogPostListenerRegistry;
import de.benjaminborbe.tools.osgi.service.RegistryServiceTracker;

@Singleton
public class MicroblogServiceTracker extends RegistryServiceTracker<MicroblogPostListener> {

	public MicroblogServiceTracker(final MicroblogPostListenerRegistry registry, final BundleContext context, final Class<?> clazz) {
		super(registry, context, clazz);
	}

}
