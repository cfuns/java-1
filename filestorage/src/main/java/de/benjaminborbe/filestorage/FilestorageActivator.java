package de.benjaminborbe.filestorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.filestorage.api.FilestorageService;
import de.benjaminborbe.filestorage.guice.FilestorageModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class FilestorageActivator extends BaseBundleActivator {

	@Inject
	private FilestorageService filestorageService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new FilestorageModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(FilestorageService.class, filestorageService));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new FilestorageServiceTracker(filestorageRegistry, context,
		// FilestorageService.class));
		return serviceTrackers;
	}
}
