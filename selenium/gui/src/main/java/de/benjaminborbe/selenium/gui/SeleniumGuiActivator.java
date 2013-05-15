package de.benjaminborbe.selenium.gui;

import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.selenium.gui.guice.SeleniumGuiModules;
import de.benjaminborbe.selenium.gui.service.SeleniumGuiNavigationEntry;
import de.benjaminborbe.selenium.gui.servlet.SeleniumGuiConfigurationListServlet;
import de.benjaminborbe.selenium.gui.servlet.SeleniumGuiConfigurationRunServlet;
import de.benjaminborbe.selenium.gui.servlet.SeleniumGuiConfigurationRunXmlServlet;
import de.benjaminborbe.selenium.gui.servlet.SeleniumGuiConfigurationXmlDeleteServlet;
import de.benjaminborbe.selenium.gui.servlet.SeleniumGuiConfigurationXmlListServlet;
import de.benjaminborbe.selenium.gui.servlet.SeleniumGuiConfigurationXmlShowServlet;
import de.benjaminborbe.selenium.gui.servlet.SeleniumGuiConfigurationXmlUpdateServlet;
import de.benjaminborbe.selenium.gui.servlet.SeleniumGuiConfigurationXmlUploadServlet;
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
	private SeleniumGuiConfigurationXmlUpdateServlet seleniumGuiConfigurationXmlUpdateServlet;

	@Inject
	private SeleniumGuiConfigurationXmlShowServlet seleniumGuiConfigurationXmlShowServlet;

	@Inject
	private SeleniumGuiConfigurationXmlDeleteServlet seleniumGuiConfigurationXmlDeleteServlet;

	@Inject
	private SeleniumGuiConfigurationXmlListServlet seleniumGuiConfigurationXmlListServlet;

	@Inject
	private SeleniumGuiConfigurationXmlUploadServlet seleniumGuiConfigurationXmlUploadServlet;

	@Inject
	private SeleniumGuiConfigurationRunXmlServlet seleniumGuiConfigurationRunXmlServlet;

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
		result.add(new ServletInfo(seleniumGuiConfigurationRunXmlServlet, SeleniumGuiConstants.URL_CONFIGURATION_XML_RUN));
		result.add(new ServletInfo(seleniumGuiConfigurationXmlUploadServlet, SeleniumGuiConstants.URL_CONFIGURATION_XML_UPLOAD));
		result.add(new ServletInfo(seleniumGuiConfigurationXmlListServlet, SeleniumGuiConstants.URL_CONFIGURATION_XML_LIST));
		result.add(new ServletInfo(seleniumGuiConfigurationXmlDeleteServlet, SeleniumGuiConstants.URL_CONFIGURATION_XML_DELETE));
		result.add(new ServletInfo(seleniumGuiConfigurationXmlShowServlet, SeleniumGuiConstants.URL_CONFIGURATION_XML_SHOW));
		result.add(new ServletInfo(seleniumGuiConfigurationXmlUpdateServlet, SeleniumGuiConstants.URL_CONFIGURATION_XML_UPDATE));

		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationEntry.class, seleniumGuiNavigationEntry));
		return result;
	}
}
