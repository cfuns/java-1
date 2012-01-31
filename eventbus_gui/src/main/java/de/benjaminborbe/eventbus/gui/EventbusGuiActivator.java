package de.benjaminborbe.eventbus.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.eventbus.gui.guice.EventbusGuiModules;
import de.benjaminborbe.eventbus.gui.servlet.EventbusGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class EventbusGuiActivator extends HttpBundleActivator {

	@Inject
	private EventbusGuiServlet eventbusGuiServlet;

	public EventbusGuiActivator() {
		super("eventbus");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new EventbusGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(eventbusGuiServlet, "/"));
		return result;
	}

}
