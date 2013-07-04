package de.benjaminborbe.task.core;

import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.core.guice.TaskModules;
import de.benjaminborbe.task.core.service.TaskSearchServiceComponent;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
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
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(SearchServiceComponent.class, taskSearchServiceComponent, taskSearchServiceComponent.getClass().getName()));
		result.add(new ServiceInfo(TaskService.class, taskService));
		return result;
	}

}
