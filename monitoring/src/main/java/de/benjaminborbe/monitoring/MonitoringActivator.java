package de.benjaminborbe.monitoring;

import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.osgi.framework.BundleContext;
import com.google.inject.Inject;
import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.monitoring.guice.MonitoringModules;
import de.benjaminborbe.monitoring.service.MonitoringCronJob;
import de.benjaminborbe.monitoring.servlet.MonitoringServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class MonitoringActivator extends HttpBundleActivator {

	@Inject
	private MonitoringServlet monitoringServlet;

	@Inject
	private MonitoringCronJob monitoringCronJob;

	public MonitoringActivator() {
		super("monitoring");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new MonitoringModules(context);
	}

	@Override
	protected Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		// monitor cronjob
		{
			final Properties props = new Properties();
			props.put("name", monitoringCronJob.getClass().getName());
			result.add(new ServiceInfo(CronJob.class, monitoringCronJob, props));
		}
		return result;
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(monitoringServlet, "/"));
		return result;
	}

}
