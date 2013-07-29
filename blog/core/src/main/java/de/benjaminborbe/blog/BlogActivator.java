package de.benjaminborbe.blog;

import de.benjaminborbe.blog.api.BlogService;
import de.benjaminborbe.blog.guice.BlogModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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

}
