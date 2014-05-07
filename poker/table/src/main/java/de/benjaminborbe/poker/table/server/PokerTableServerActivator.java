package de.benjaminborbe.poker.table.server;

import de.benjaminborbe.poker.table.server.guice.PokerTableServerModules;
import de.benjaminborbe.poker.table.server.servlet.PokerTableHomeNoCacheJsServlet;
import de.benjaminborbe.poker.table.server.servlet.PokerTableHomeServlet;
import de.benjaminborbe.poker.table.server.servlet.PokerTableStatusServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PokerTableServerActivator extends HttpBundleActivator {

	@Inject
	private PokerTableStatusServlet statusCallService;

	@Inject
	private PokerTableHomeNoCacheJsServlet pokerTableHomeNoCacheJsServlet;

	@Inject
	private PokerTableHomeServlet pokerTableHomeServlet;

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
		result.add(new ServletInfo(pokerTableHomeServlet, "/Home.html"));
		result.add(new ServletInfo(pokerTableHomeNoCacheJsServlet, "/Home/Home.nocache.js"));
		result.add(new ServletInfo(statusCallService, "/Home/statusService"));
		return result;
	}

	@Override
	protected Collection<ResourceInfo> getResouceInfos() {
		final Set<ResourceInfo> result = new HashSet<ResourceInfo>(super.getResouceInfos());
		result.add(new ResourceInfo("/Home", "Home"));
		result.add(new ResourceInfo("/images", "images"));
		return result;
	}

}
