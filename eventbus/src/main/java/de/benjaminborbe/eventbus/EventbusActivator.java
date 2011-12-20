package de.benjaminborbe.eventbus;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import com.google.inject.Inject;

import de.benjaminborbe.eventbus.api.EventBusService;
import de.benjaminborbe.eventbus.guice.EventbusModules;
import de.benjaminborbe.eventbus.servlet.EventbusServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class EventbusActivator extends HttpBundleActivator {

	@Inject
	private EventbusServlet eventbusServlet;

	@Inject
	private EventBusService eventBusService;

	public EventbusActivator() {
		super("eventbus");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new EventbusModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(eventbusServlet, "/"));
		return result;
	}

	@Override
	protected Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(EventBusService.class, eventBusService));
		return result;
	}
}
