package de.benjaminborbe.distributed.search.gui;

import com.google.inject.Inject;
import de.benjaminborbe.distributed.search.gui.guice.DistributedSearchGuiModules;
import de.benjaminborbe.distributed.search.gui.service.DistributedSearchGuiNavigationEntry;
import de.benjaminborbe.distributed.search.gui.servlet.DistributedSearchGuiPageRebuildServlet;
import de.benjaminborbe.distributed.search.gui.servlet.DistributedSearchGuiPageServlet;
import de.benjaminborbe.distributed.search.gui.servlet.DistributedSearchGuiRebuildAllServlet;
import de.benjaminborbe.distributed.search.gui.servlet.DistributedSearchGuiRebuildIndexServlet;
import de.benjaminborbe.distributed.search.gui.servlet.DistributedSearchGuiServlet;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DistributedSearchGuiActivator extends HttpBundleActivator {

	@Inject
	private DistributedSearchGuiRebuildAllServlet distributedSearchGuiRebuildAllServlet;

	@Inject
	private DistributedSearchGuiPageRebuildServlet distributedSearchGuiPageRebuildServlet;

	@Inject
	private DistributedSearchGuiPageServlet distributedSearchGuiPageServlet;

	@Inject
	private DistributedSearchGuiRebuildIndexServlet distributedSearchGuiRebuildIndexServlet;

	@Inject
	private DistributedSearchGuiServlet distributedSearchGuiServlet;

	@Inject
	private DistributedSearchGuiNavigationEntry distributedSearchGuiNavigationEntry;

	public DistributedSearchGuiActivator() {
		super(DistributedSearchGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new DistributedSearchGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<>(super.getServletInfos());
		result.add(new ServletInfo(distributedSearchGuiServlet, DistributedSearchGuiConstants.URL_HOME));
		result.add(new ServletInfo(distributedSearchGuiPageServlet, DistributedSearchGuiConstants.URL_PAGE));
		result.add(new ServletInfo(distributedSearchGuiRebuildAllServlet, DistributedSearchGuiConstants.URL_REBUILD_ALL));
		result.add(new ServletInfo(distributedSearchGuiRebuildIndexServlet, DistributedSearchGuiConstants.URL_REBUILD_INDEX));
		result.add(new ServletInfo(distributedSearchGuiPageRebuildServlet, DistributedSearchGuiConstants.URL_REBUILD_PAGE));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationEntry.class, distributedSearchGuiNavigationEntry));
		return result;
	}
}
