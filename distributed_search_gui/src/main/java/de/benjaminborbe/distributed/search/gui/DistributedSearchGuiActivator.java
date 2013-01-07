package de.benjaminborbe.distributed.search.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.distributed.search.gui.guice.DistributedSearchGuiModules;
import de.benjaminborbe.distributed.search.gui.servlet.DistributedSearchGuiRebuildIndexServlet;
import de.benjaminborbe.distributed.search.gui.servlet.DistributedSearchGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class DistributedSearchGuiActivator extends HttpBundleActivator {

	@Inject
	private DistributedSearchGuiRebuildIndexServlet distributedSearchGuiRebuildIndexServlet;

	@Inject
	private DistributedSearchGuiServlet distributedSearchGuiServlet;

	public DistributedSearchGuiActivator() {
		super(DistributedSearchGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new DistributedSearchGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(distributedSearchGuiServlet, DistributedSearchGuiConstants.URL_HOME));
		result.add(new ServletInfo(distributedSearchGuiRebuildIndexServlet, DistributedSearchGuiConstants.URL_REBUILD_INDEX));
		return result;
	}

}
