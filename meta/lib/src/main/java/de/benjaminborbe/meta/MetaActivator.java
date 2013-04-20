package de.benjaminborbe.meta;

import org.osgi.framework.BundleContext;
import org.osgi.service.obr.RepositoryAdmin;
import org.osgi.service.obr.Requirement;
import org.osgi.service.obr.Resolver;
import org.osgi.service.obr.Resource;

import javax.inject.Inject;
import com.google.inject.Provider;

import de.benjaminborbe.meta.guice.MetaModules;
import de.benjaminborbe.meta.util.BundleResolver;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.util.ThreadPoolExecuter;
import de.benjaminborbe.tools.util.ThreadPoolExecuterBuilder;

public class MetaActivator extends BaseBundleActivator {

	private final class DeployRunnable implements Runnable {

		private final RepositoryAdmin repoAdmin;

		private final String bundle;

		private DeployRunnable(final RepositoryAdmin repoAdmin, final String bundle) {
			this.repoAdmin = repoAdmin;
			this.bundle = bundle;
		}

		@Override
		public void run() {
			deploy(repoAdmin, bundle);
		}
	}

	private static final int THREAD_AMOUNT = 1;

	@Inject
	private BundleResolver bundleResolver;

	@Inject
	private Provider<RepositoryAdmin> repositoryAdminProvider;

	@Inject
	private ThreadPoolExecuterBuilder threadPoolExecuterBuilder;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new MetaModules(context);
	}

	@Override
	protected void onStarted() {
		super.onStarted();

		logger.info("onStarted - start");
		try {
			final ThreadPoolExecuter threadPoolExecuter = threadPoolExecuterBuilder.build("obr deploy bundle", THREAD_AMOUNT);

			final RepositoryAdmin repoAdmin = repositoryAdminProvider.get();
			for (final String bundle : bundleResolver.getBundleSymbolicNames()) {
				threadPoolExecuter.execute(new DeployRunnable(repoAdmin, bundle));
			}

			threadPoolExecuter.shutDown();
		}
		catch (final Exception e) {
			logger.warn("deploy bunldes failed!", e);
		}
		logger.info("onStarted - end");
	}

	protected void deploy(final RepositoryAdmin repoAdmin, final String name) {
		final Resolver resolver = repoAdmin.resolver();
		final Resource[] resources = repoAdmin.discoverResources("(symbolicname=" + name + ")");

		logger.trace("found " + resources.length + " resources");
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
