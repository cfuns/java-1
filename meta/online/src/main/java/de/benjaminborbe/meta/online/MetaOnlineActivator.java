package de.benjaminborbe.meta.online;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.service.obr.RepositoryAdmin;
import org.osgi.service.obr.Requirement;
import org.osgi.service.obr.Resolver;
import org.osgi.service.obr.Resource;

import com.google.inject.Inject;
import com.google.inject.Provider;

import de.benjaminborbe.meta.online.guice.MetaOnlineModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;

public class MetaOnlineActivator extends BaseBundleActivator {

	@Inject
	private Provider<RepositoryAdmin> repositoryAdminProvider;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new MetaOnlineModules(context);
	}

	@Override
	protected void onStarted() {
		super.onStarted();

		logger.info("onStarted - start");
		try {

			final List<String> bundles = new ArrayList<String>();

			bundles.add("de.benjaminborbe.portfolio.gui");
			bundles.add("de.benjaminborbe.authentication.gui");
			bundles.add("de.benjaminborbe.authorization.gui");

			for (final String bundle : bundles) {
				deploy(bundle);
			}
		}
		catch (final Exception e) {
			e.printStackTrace();
		}
		logger.info("onStarted - end");
	}

	protected void deploy(final String name) {
		final RepositoryAdmin repoAdmin = repositoryAdminProvider.get();
		final Resolver resolver = repoAdmin.resolver();
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
