package de.benjaminborbe.dns.core;

import javax.inject.Inject;
import de.benjaminborbe.dns.api.DnsService;
import de.benjaminborbe.dns.core.check.DnsServerMonitoringCheck;
import de.benjaminborbe.dns.core.guice.DnsCoreModules;
import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DnsCoreActivator extends BaseBundleActivator {

	@Inject
	private DnsServerMonitoringCheck dnsServerMonitoringCheck;

	@Inject
	private DnsService dnsService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new DnsCoreModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(DnsService.class, dnsService));
		result.add(new ServiceInfo(MonitoringCheck.class, dnsServerMonitoringCheck));
		return result;
	}

}
