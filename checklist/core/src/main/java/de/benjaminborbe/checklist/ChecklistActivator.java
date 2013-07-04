package de.benjaminborbe.checklist;

import de.benjaminborbe.checklist.api.ChecklistService;
import de.benjaminborbe.checklist.guice.ChecklistModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
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

}
