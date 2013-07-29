package de.benjaminborbe.distributed.index.gui;

import de.benjaminborbe.distributed.index.gui.guice.DistributedIndexGuiModules;
import de.benjaminborbe.distributed.index.gui.service.DistributedIndexGuiNavigationEntry;
import de.benjaminborbe.distributed.index.gui.servlet.DistributedIndexGuiEntryInfoServlet;
import de.benjaminborbe.distributed.index.gui.servlet.DistributedIndexGuiServlet;
import de.benjaminborbe.distributed.index.gui.servlet.DistributedIndexGuiWordInfoServlet;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DistributedIndexGuiActivator extends HttpBundleActivator {

	@Inject
	private DistributedIndexGuiNavigationEntry distributedIndexGuiNavigationEntry;

	@Inject
	private DistributedIndexGuiEntryInfoServlet distributedIndexGuiEntryInfoServlet;

	@Inject
	private DistributedIndexGuiServlet distributedIndexGuiServlet;

	@Inject
	private DistributedIndexGuiWordInfoServlet distributedIndexGuiWordInfoServlet;

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
		result.add(new ServletInfo(distributedIndexGuiServlet, DistributedIndexGuiConstants.URL_HOME));
		result.add(new ServletInfo(distributedIndexGuiEntryInfoServlet, DistributedIndexGuiConstants.URL_ENTRY_INFO));
		result.add(new ServletInfo(distributedIndexGuiWordInfoServlet, DistributedIndexGuiConstants.URL_WORD_INFO));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationEntry.class, distributedIndexGuiNavigationEntry));
		return result;
	}

}
