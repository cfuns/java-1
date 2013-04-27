package de.benjaminborbe.cms;

import de.benjaminborbe.cms.api.CmsService;
import de.benjaminborbe.cms.guice.CmsModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CmsActivator extends BaseBundleActivator {

	@Inject
	private CmsService cmsService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new CmsModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(CmsService.class, cmsService));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<>(super.getServiceTrackers(context));
		// serviceTrackers.add(new CmsServiceTracker(cmsRegistry, context,
		// CmsService.class));
		return serviceTrackers;
	}
}
