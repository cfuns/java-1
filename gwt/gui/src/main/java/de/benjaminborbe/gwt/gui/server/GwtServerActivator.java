package de.benjaminborbe.gwt.gui.server;

import de.benjaminborbe.gwt.gui.server.guice.GwtServerModules;
import de.benjaminborbe.gwt.gui.server.servlet.GwtHomeNoCacheJsServlet;
import de.benjaminborbe.gwt.gui.server.servlet.GwtHomeServlet;
import de.benjaminborbe.gwt.gui.server.servlet.GwtServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class GwtServerActivator extends HttpBundleActivator {

	@Inject
	private GwtServlet gwtServlet;

	@Inject
	private GwtHomeNoCacheJsServlet gwtHomeNoCacheJsServlet;

	@Inject
	private GwtHomeServlet gwtHomeServlet;

	public GwtServerActivator() {
		super(GwtServerConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new GwtServerModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<>();

		result.add(new ServletInfo(gwtServlet, GwtServerConstants.URL_HOME));
		result.add(new ServletInfo(gwtHomeServlet, GwtServerConstants.URL_HOME_HTML));
		result.add(new ServletInfo(gwtHomeNoCacheJsServlet, GwtServerConstants.URL_HOME_JS));
		return result;
	}

	@Override
	protected Collection<ResourceInfo> getResouceInfos() {
		final Set<ResourceInfo> result = new HashSet<>(super.getResouceInfos());
		result.add(new ResourceInfo("/Home", "Home"));
		return result;
	}

}
