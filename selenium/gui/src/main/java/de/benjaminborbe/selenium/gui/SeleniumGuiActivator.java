package de.benjaminborbe.selenium.gui;

import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.selenium.gui.guice.SeleniumGuiModules;
import de.benjaminborbe.selenium.gui.service.SeleniumGuiNavigationEntry;
import de.benjaminborbe.selenium.gui.servlet.SeleniumGuiConfigurationListServlet;
import de.benjaminborbe.selenium.gui.servlet.SeleniumGuiConfigurationRunServlet;
import de.benjaminborbe.selenium.gui.servlet.SeleniumGuiConfigurationUploadServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SeleniumGuiActivator extends HttpBundleActivator {

	@Inject
	private SeleniumGuiConfigurationUploadServlet seleniumGuiConfigurationUploadServlet;

	@Inject
	private SeleniumGuiConfigurationRunServlet seleniumGuiConfigurationRunServlet;

	@Inject
	private SeleniumGuiConfigurationListServlet seleniumGuiConfigurationListServlet;

	@Inject
	private SeleniumGuiNavigationEntry seleniumGuiNavigationEntry;

	public SeleniumGuiActivator() {
		super(SeleniumGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new SeleniumGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<>(super.getServletInfos());
		result.add(new ServletInfo(seleniumGuiConfigurationListServlet, SeleniumGuiConstants.URL_CONFIGURATION_LIST));
		result.add(new ServletInfo(seleniumGuiConfigurationRunServlet, SeleniumGuiConstants.URL_CONFIGURATION_RUN));
		result.add(new ServletInfo(seleniumGuiConfigurationUploadServlet, SeleniumGuiConstants.URL_CONFIGURATION_UPLOAD));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationEntry.class, seleniumGuiNavigationEntry));
		return result;
	}
}
