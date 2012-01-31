package de.benjaminborbe.search.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.search.gui.guice.SearchGuiModules;
import de.benjaminborbe.search.gui.servlet.SearchGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class SearchGuiActivator extends HttpBundleActivator {

	@Inject
	private SearchGuiServlet searchGuiServlet;

	public SearchGuiActivator() {
		super("search");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new SearchGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(searchGuiServlet, "/"));
		return result;
	}

}
