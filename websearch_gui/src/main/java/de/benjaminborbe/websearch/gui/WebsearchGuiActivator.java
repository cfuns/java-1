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
import de.benjaminborbe.websearch.gui.servlet.WebsearchGuiClearIndexServlet;
import de.benjaminborbe.websearch.gui.servlet.WebsearchGuiExpireAllPagesServlet;
import de.benjaminborbe.websearch.gui.servlet.WebsearchGuiExpirePageServlet;
import de.benjaminborbe.websearch.gui.servlet.WebsearchGuiListPagesServlet;
import de.benjaminborbe.websearch.gui.servlet.WebsearchGuiPageRefreshServlet;
import de.benjaminborbe.websearch.gui.servlet.WebsearchGuiRefreshPagesServlet;
import de.benjaminborbe.websearch.gui.servlet.WebsearchGuiServlet;

public class WebsearchGuiActivator extends HttpBundleActivator {

	@Inject
	private WebsearchGuiServlet websearchGuiServlet;

	@Inject
	private WebsearchGuiClearIndexServlet websearchGuiClearIndexServlet;

	@Inject
	private WebsearchGuiRefreshPagesServlet websearchGuiRefreshPagesServlet;

	@Inject
	private WebsearchGuiListPagesServlet websearchGuiListPagesServlet;

	@Inject
	private WebsearchGuiExpireAllPagesServlet websearchGuiExpireAllPagesServlet;

	@Inject
	private WebsearchGuiExpirePageServlet websearchGuiExpirePageServlet;

	@Inject
	private WebsearchGuiPageRefreshServlet websearchGuiPageRefreshServlet;

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
		return result;
	}

}
