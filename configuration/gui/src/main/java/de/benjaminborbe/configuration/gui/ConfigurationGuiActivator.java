package de.benjaminborbe.configuration.gui;

import de.benjaminborbe.configuration.gui.guice.ConfigurationGuiModules;
import de.benjaminborbe.configuration.gui.servlet.ConfigurationGuiListServlet;
import de.benjaminborbe.configuration.gui.servlet.ConfigurationGuiUpdateServlet;
import de.benjaminborbe.configuration.gui.util.ConfigurationGuiNavigationEntry;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ConfigurationGuiActivator extends HttpBundleActivator {

	@Inject
	private ConfigurationGuiUpdateServlet configurationGuiUpdateServlet;

	@Inject
	private ConfigurationGuiListServlet configurationGuiListServlet;

	@Inject
	private ConfigurationGuiNavigationEntry configurationGuiNavigationEntry;

	public ConfigurationGuiActivator() {
		super(ConfigurationGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new ConfigurationGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<>(super.getServletInfos());
		result.add(new ServletInfo(configurationGuiListServlet, ConfigurationGuiConstants.URL_LIST));
		result.add(new ServletInfo(configurationGuiUpdateServlet, ConfigurationGuiConstants.URL_UPDATE));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationEntry.class, configurationGuiNavigationEntry));
		return result;
	}

	@Override
	protected Collection<ResourceInfo> getResouceInfos() {
		final Set<ResourceInfo> result = new HashSet<>(super.getResouceInfos());
		result.add(new ResourceInfo("/css", "css"));
		return result;
	}
}
