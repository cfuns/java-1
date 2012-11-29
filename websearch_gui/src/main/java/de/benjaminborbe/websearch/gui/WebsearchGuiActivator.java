package de.benjaminborbe.websearch.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;
import de.benjaminborbe.websearch.gui.guice.WebsearchGuiModules;
import de.benjaminborbe.websearch.gui.servlet.WebsearchGuiConfigurationCreateServlet;
import de.benjaminborbe.websearch.gui.servlet.WebsearchGuiConfigurationDeleteServlet;
import de.benjaminborbe.websearch.gui.servlet.WebsearchGuiConfigurationListServlet;
import de.benjaminborbe.websearch.gui.servlet.WebsearchGuiConfigurationUpdateServlet;
import de.benjaminborbe.websearch.gui.servlet.WebsearchGuiIndexClearServlet;
import de.benjaminborbe.websearch.gui.servlet.WebsearchGuiPageExpireAllServlet;
import de.benjaminborbe.websearch.gui.servlet.WebsearchGuiPageExpireServlet;
import de.benjaminborbe.websearch.gui.servlet.WebsearchGuiListPagesServlet;
import de.benjaminborbe.websearch.gui.servlet.WebsearchGuiPageRefreshServlet;
import de.benjaminborbe.websearch.gui.servlet.WebsearchGuiPageRefreshAllServlet;
import de.benjaminborbe.websearch.gui.servlet.WebsearchGuiServlet;

public class WebsearchGuiActivator extends HttpBundleActivator {

	@Inject
	private WebsearchGuiServlet websearchGuiServlet;

	@Inject
	private WebsearchGuiIndexClearServlet websearchGuiClearIndexServlet;

	@Inject
	private WebsearchGuiPageRefreshAllServlet websearchGuiRefreshPagesServlet;

	@Inject
	private WebsearchGuiListPagesServlet websearchGuiListPagesServlet;

	@Inject
	private WebsearchGuiPageExpireAllServlet websearchGuiExpireAllPagesServlet;

	@Inject
	private WebsearchGuiPageExpireServlet websearchGuiExpirePageServlet;

	@Inject
	private WebsearchGuiPageRefreshServlet websearchGuiPageRefreshServlet;

	@Inject
	private WebsearchGuiConfigurationCreateServlet websearchGuiConfigurationCreateServlet;

	@Inject
	private WebsearchGuiConfigurationDeleteServlet websearchGuiConfigurationDeleteServlet;

	@Inject
	private WebsearchGuiConfigurationListServlet websearchGuiConfigurationListServlet;

	@Inject
	private WebsearchGuiConfigurationUpdateServlet websearchGuiConfigurationUpdateServlet;

	public WebsearchGuiActivator() {
		super(WebsearchGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new WebsearchGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(websearchGuiServlet, WebsearchGuiConstants.URL_HOME));
		result.add(new ServletInfo(websearchGuiRefreshPagesServlet, WebsearchGuiConstants.URL_REFRESH));
		result.add(new ServletInfo(websearchGuiListPagesServlet, WebsearchGuiConstants.URL_LIST));
		result.add(new ServletInfo(websearchGuiExpirePageServlet, WebsearchGuiConstants.URL_EXPIRE));
		result.add(new ServletInfo(websearchGuiExpireAllPagesServlet, WebsearchGuiConstants.URL_EXPIRE_ALL));
		result.add(new ServletInfo(websearchGuiClearIndexServlet, WebsearchGuiConstants.URL_CLEAR_INDEX));
		result.add(new ServletInfo(websearchGuiPageRefreshServlet, WebsearchGuiConstants.URL_REFRESH_PAGE));
		result.add(new ServletInfo(websearchGuiConfigurationCreateServlet, WebsearchGuiConstants.URL_CONFIGURATION_CREATE));
		result.add(new ServletInfo(websearchGuiConfigurationDeleteServlet, WebsearchGuiConstants.URL_CONFIGURATION_DELETE));
		result.add(new ServletInfo(websearchGuiConfigurationListServlet, WebsearchGuiConstants.URL_CONFIGURATION_LIST));
		result.add(new ServletInfo(websearchGuiConfigurationUpdateServlet, WebsearchGuiConstants.URL_CONFIGURATION_UPDATE));
		return result;
	}

}
