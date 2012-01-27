package de.benjaminborbe.configuration.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.configuration.gui.guice.ConfigurationGuiModules;
import de.benjaminborbe.configuration.gui.servlet.ConfigurationGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class ConfigurationGuiActivator extends HttpBundleActivator {

	@Inject
	private ConfigurationGuiServlet configurationGuiServlet;

	public ConfigurationGuiActivator() {
		super("configuration");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new ConfigurationGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(configurationGuiServlet, "/"));
		return result;
	}

}
