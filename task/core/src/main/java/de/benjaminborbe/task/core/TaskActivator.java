package de.benjaminborbe.task.core;

import com.google.inject.Inject;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.core.guice.TaskModules;
import de.benjaminborbe.task.core.service.TaskSearchServiceComponent;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TaskActivator extends BaseBundleActivator {

	@Inject
	private TaskService taskService;

	@Inject
	private TaskSearchServiceComponent taskSearchServiceComponent;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new TaskModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(SearchServiceComponent.class, taskSearchServiceComponent, taskSearchServiceComponent.getClass().getName()));
		result.add(new ServiceInfo(TaskService.class, taskService));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new TaskServiceTracker(taskRegistry, context,
		// TaskService.class));
		return serviceTrackers;
	}
}
