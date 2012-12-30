package de.benjaminborbe.distributed.index.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.distributed.index.gui.guice.DistributedIndexGuiModules;
import de.benjaminborbe.distributed.index.gui.servlet.DistributedIndexGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class DistributedIndexGuiActivator extends HttpBundleActivator {

	@Inject
	private DistributedIndexGuiServlet distributed_indexGuiServlet;

	public DistributedIndexGuiActivator() {
		super(DistributedIndexGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new DistributedIndexGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(distributed_indexGuiServlet, DistributedIndexGuiConstants.HOME_URL));
		return result;
	}

	// @Override
	// protected Collection<FilterInfo> getFilterInfos() {
	// final Set<FilterInfo> result = new HashSet<FilterInfo>(super.getFilterInfos());
	// result.add(new FilterInfo(distributed_indexFilter, ".*", 998));
	// return result;
	// }

	// @Override
	// protected Collection<ResourceInfo> getResouceInfos() {
	// final Set<ResourceInfo> result = new HashSet<ResourceInfo>(super.getResouceInfos());
	// // result.add(new ResourceInfo("/css", "css"));
	// // result.add(new ResourceInfo("/js", "js"));
	// // result.add(new ResourceInfo("/images", "images"));
	// return result;
	// }
}
