package de.benjaminborbe.meta;

import org.osgi.framework.BundleContext;
import org.osgi.service.obr.RepositoryAdmin;
import org.osgi.service.obr.Requirement;
import org.osgi.service.obr.Resolver;
import org.osgi.service.obr.Resource;

import com.google.inject.Inject;
import com.google.inject.Provider;

import de.benjaminborbe.meta.guice.MetaModules;
import de.benjaminborbe.meta.util.BundleResolver;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;

public class MetaActivator extends BaseBundleActivator {

	@Inject
	private BundleResolver bundleResolver;

	@Inject
	private Provider<RepositoryAdmin> repositoryAdminProvider;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new MetaModules(context);
	}

	@Override
	protected void onStarted() {
		super.onStarted();

		logger.info("onStarted - start");
		try {

			final RepositoryAdmin repoAdmin = repositoryAdminProvider.get();
			final Resolver resolver = repoAdmin.resolver();
			for (final String bundle : bundleResolver.getBundleSymbolicNames()) {
				deploy(repoAdmin, resolver, bundle);
			}
		}
		catch (final Exception e) {
			logger.warn("deploy bunldes failed!", e);
		}
		logger.info("onStarted - end");
	}

	protected void deploy(final RepositoryAdmin repoAdmin, final Resolver resolver, final String name) {
		final Resource[] resources = repoAdmin.discoverResources("(symbolicname=" + name + ")");

		logger.debug("found " + resources.length + " resources");
		for (final Resource resource : resources) {
			resolver.add(resource);
			if (resolver.resolve()) {
				resolver.deploy(true);
			}
			else {
				final Requirement[] reqs = resolver.getUnsatisfiedRequirements();
				for (int i = 0; i < reqs.length; i++) {
					logger.warn("Unable to resolve: " + reqs[i]);
				}
			}
		}

	}
}
