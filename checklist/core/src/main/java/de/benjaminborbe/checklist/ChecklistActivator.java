package de.benjaminborbe.checklist;

import javax.inject.Inject;
import de.benjaminborbe.checklist.api.ChecklistService;
import de.benjaminborbe.checklist.guice.ChecklistModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ChecklistActivator extends BaseBundleActivator {

	@Inject
	private ChecklistService checklistService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new ChecklistModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(ChecklistService.class, checklistService));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<>(super.getServiceTrackers(context));
		// serviceTrackers.add(new ChecklistServiceTracker(checklistRegistry, context,
		// ChecklistService.class));
		return serviceTrackers;
	}
}
