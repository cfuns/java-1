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
import de.benjaminborbe.websearch.gui.servlet.WebsearchGuiExpireAllPagesServlet;
import de.benjaminborbe.websearch.gui.servlet.WebsearchGuiExpirePageServlet;
import de.benjaminborbe.websearch.gui.servlet.WebsearchGuiListPagesServlet;
import de.benjaminborbe.websearch.gui.servlet.WebsearchGuiRefreshPagesServlet;
import de.benjaminborbe.websearch.gui.servlet.WebsearchGuiServlet;

public class WebsearchGuiActivator extends HttpBundleActivator {

	@Inject
	private WebsearchGuiServlet websearchGuiServlet;

	@Inject
	private WebsearchGuiRefreshPagesServlet websearchGuiRefreshPagesServlet;

	@Inject
	private WebsearchGuiListPagesServlet websearchGuiListPagesServlet;

	@Inject
	private WebsearchGuiExpireAllPagesServlet websearchGuiExpireAllPagesServlet;

	@Inject
	private WebsearchGuiExpirePageServlet websearchGuiExpirePageServlet;

	public WebsearchGuiActivator() {
		super("websearch");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new WebsearchGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(websearchGuiServlet, "/"));
		result.add(new ServletInfo(websearchGuiRefreshPagesServlet, "/refresh"));
		result.add(new ServletInfo(websearchGuiListPagesServlet, "/list"));
		result.add(new ServletInfo(websearchGuiExpirePageServlet, "/expire"));
		result.add(new ServletInfo(websearchGuiExpireAllPagesServlet, "/expireAll"));
		return result;
	}

}
