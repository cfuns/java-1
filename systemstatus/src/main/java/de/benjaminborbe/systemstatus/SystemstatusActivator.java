package de.benjaminborbe.systemstatus;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import com.google.inject.Inject;

import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.systemstatus.api.SystemstatusService;
import de.benjaminborbe.systemstatus.guice.SystemstatusModules;
import de.benjaminborbe.systemstatus.service.SystemstatusDiskspaceMonitoringCheck;
import de.benjaminborbe.systemstatus.service.SystemstatusHeapMemoryMonitoringCheck;
import de.benjaminborbe.systemstatus.service.SystemstatusNonHeapMemoryMonitoringCheck;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class SystemstatusActivator extends BaseBundleActivator {

	@Inject
	private SystemstatusDiskspaceMonitoringCheck systemstatusDiskspaceMonitoringCheck;

	@Inject
	private SystemstatusHeapMemoryMonitoringCheck systemstatusHeapMemoryMonitoringCheck;

	@Inject
	private SystemstatusNonHeapMemoryMonitoringCheck systemstatusNonHeapMemoryMonitoringCheck;

	@Inject
	private SystemstatusService systemstatusService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new SystemstatusModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(SystemstatusService.class, systemstatusService));
		result.add(new ServiceInfo(MonitoringCheck.class, systemstatusDiskspaceMonitoringCheck, systemstatusDiskspaceMonitoringCheck.getClass().getName()));
		result.add(new ServiceInfo(MonitoringCheck.class, systemstatusHeapMemoryMonitoringCheck, systemstatusHeapMemoryMonitoringCheck.getClass().getName()));
		result.add(new ServiceInfo(MonitoringCheck.class, systemstatusNonHeapMemoryMonitoringCheck, systemstatusNonHeapMemoryMonitoringCheck.getClass().getName()));
		return result;
	}

}
