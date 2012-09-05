package de.benjaminborbe.blog;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.blog.api.BlogService;
import de.benjaminborbe.blog.guice.BlogModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class BlogActivator extends BaseBundleActivator {

	@Inject
	private BlogService blogService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new BlogModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(BlogService.class, blogService));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new BlogServiceTracker(blogRegistry, context,
		// BlogService.class));
		return serviceTrackers;
	}
}
