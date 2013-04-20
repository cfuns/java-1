package de.benjaminborbe.systemstatus.gui;

import javax.inject.Inject;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.systemstatus.gui.guice.SystemstatusGuiModules;
import de.benjaminborbe.systemstatus.gui.service.SystemstatusGuiNavigationEntry;
import de.benjaminborbe.systemstatus.gui.servlet.SystemstatusGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SystemstatusGuiActivator extends HttpBundleActivator {

	@Inject
	private SystemstatusGuiServlet systemstatusGuiServlet;

	@Inject
	private SystemstatusGuiNavigationEntry systemstatusGuiNavigationEntry;

	public SystemstatusGuiActivator() {
		super(SystemstatusGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new SystemstatusGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<>(super.getServletInfos());
		result.add(new ServletInfo(systemstatusGuiServlet, SystemstatusGuiConstants.URL_HOME));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationEntry.class, systemstatusGuiNavigationEntry));
		return result;
	}
}
