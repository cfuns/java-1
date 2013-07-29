package de.benjaminborbe.systemstatus.core;

import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.systemstatus.api.SystemstatusService;
import de.benjaminborbe.systemstatus.core.guice.SystemstatusModules;
import de.benjaminborbe.systemstatus.core.service.SystemstatusDiskspaceFreeMbMonitoringCheck;
import de.benjaminborbe.systemstatus.core.service.SystemstatusDiskspaceFreePercentMonitoringCheck;
import de.benjaminborbe.systemstatus.core.service.SystemstatusHeapMemoryFreeMbMonitoringCheck;
import de.benjaminborbe.systemstatus.core.service.SystemstatusHeapMemoryFreePercentMonitoringCheck;
import de.benjaminborbe.systemstatus.core.service.SystemstatusNonHeapMemoryFreeMbMonitoringCheck;
import de.benjaminborbe.systemstatus.core.service.SystemstatusNonHeapMemoryFreePercentMonitoringCheck;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SystemstatusActivator extends BaseBundleActivator {

	@Inject
	private SystemstatusDiskspaceFreeMbMonitoringCheck systemstatusDiskspaceFreeMbMonitoringCheck;

	@Inject
	private SystemstatusDiskspaceFreePercentMonitoringCheck systemstatusDiskspaceFreePercentMonitoringCheck;

	@Inject
	private SystemstatusHeapMemoryFreeMbMonitoringCheck systemstatusHeapMemoryFreeMbMonitoringCheck;

	@Inject
	private SystemstatusHeapMemoryFreePercentMonitoringCheck systemstatusHeapMemoryFreePercentMonitoringCheck;

	@Inject
	private SystemstatusNonHeapMemoryFreeMbMonitoringCheck systemstatusNonHeapMemoryFreeMbMonitoringCheck;

	@Inject
	private SystemstatusNonHeapMemoryFreePercentMonitoringCheck systemstatusNonHeapMemoryFreePercentMonitoringCheck;

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
		result.add(new ServiceInfo(MonitoringCheck.class, systemstatusDiskspaceFreeMbMonitoringCheck, systemstatusDiskspaceFreeMbMonitoringCheck.getClass().getName()));
		result.add(new ServiceInfo(MonitoringCheck.class, systemstatusDiskspaceFreePercentMonitoringCheck, systemstatusDiskspaceFreePercentMonitoringCheck.getClass().getName()));

		result.add(new ServiceInfo(MonitoringCheck.class, systemstatusHeapMemoryFreeMbMonitoringCheck, systemstatusHeapMemoryFreeMbMonitoringCheck.getClass().getName()));
		result.add(new ServiceInfo(MonitoringCheck.class, systemstatusHeapMemoryFreePercentMonitoringCheck, systemstatusHeapMemoryFreePercentMonitoringCheck.getClass().getName()));
		result.add(new ServiceInfo(MonitoringCheck.class, systemstatusNonHeapMemoryFreeMbMonitoringCheck, systemstatusNonHeapMemoryFreeMbMonitoringCheck.getClass().getName()));
		result
			.add(new ServiceInfo(MonitoringCheck.class, systemstatusNonHeapMemoryFreePercentMonitoringCheck, systemstatusNonHeapMemoryFreePercentMonitoringCheck.getClass().getName()));
		return result;
	}

}
