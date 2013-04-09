package de.benjaminborbe.worktime;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.worktime.api.WorktimeService;
import de.benjaminborbe.worktime.guice.WorktimeModules;
import de.benjaminborbe.worktime.service.WorktimeCronJob;

public class WorktimeActivator extends BaseBundleActivator {

	@Inject
	private WorktimeCronJob worktimeCronJob;

	@Inject
	private WorktimeService worktimeService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new WorktimeModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(CronJob.class, worktimeCronJob, worktimeCronJob.getClass().getName()));
		result.add(new ServiceInfo(WorktimeService.class, worktimeService));
		return result;
	}

}
