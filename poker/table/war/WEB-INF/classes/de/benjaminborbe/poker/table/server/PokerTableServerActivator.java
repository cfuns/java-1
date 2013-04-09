package de.benjaminborbe.poker.table.server;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.poker.table.server.guice.PokerTableServerModules;
import de.benjaminborbe.poker.table.server.servlet.GwtHomeNoCacheJsServlet;
import de.benjaminborbe.poker.table.server.servlet.GwtHomeServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class PokerTableServerActivator extends HttpBundleActivator {

	@Inject
	private GwtHomeNoCacheJsServlet gwtHomeNoCacheJsServlet;

	@Inject
	private GwtHomeServlet gwtHomeServlet;

	public PokerTableServerActivator() {
		super(PokerTableServerConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new PokerTableServerModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>();
		result.add(new ServletInfo(gwtHomeServlet, "/Home.html"));
		result.add(new ServletInfo(gwtHomeNoCacheJsServlet, "/Home/Home.nocache.js"));
		return result;
	}

	@Override
	protected Collection<ResourceInfo> getResouceInfos() {
		final Set<ResourceInfo> result = new HashSet<ResourceInfo>(super.getResouceInfos());
		result.add(new ResourceInfo("/Home", "Home"));
		return result;
	}

}
