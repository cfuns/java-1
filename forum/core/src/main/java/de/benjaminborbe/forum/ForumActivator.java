package de.benjaminborbe.forum;

import com.google.inject.Inject;
import de.benjaminborbe.forum.api.ForumService;
import de.benjaminborbe.forum.guice.ForumModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ForumActivator extends BaseBundleActivator {

	@Inject
	private ForumService forumService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new ForumModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(ForumService.class, forumService));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<>(super.getServiceTrackers(context));
		// serviceTrackers.add(new ForumServiceTracker(forumRegistry, context,
		// ForumService.class));
		return serviceTrackers;
	}
}
