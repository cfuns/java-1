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
import de.benjaminborbe.websearch.gui.servlet.WebsearchGuiServlet;

public class WebsearchGuiActivator extends HttpBundleActivator {

	@Inject
	private WebsearchGuiServlet websearchGuiServlet;

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
		return result;
	}

}
