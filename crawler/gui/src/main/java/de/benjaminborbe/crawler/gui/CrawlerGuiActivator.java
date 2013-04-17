package de.benjaminborbe.crawler.gui;

import com.google.inject.Inject;
import de.benjaminborbe.crawler.gui.guice.CrawlerGuiModules;
import de.benjaminborbe.crawler.gui.service.CrawlerGuiNavigationEntry;
import de.benjaminborbe.crawler.gui.servlet.CrawlerGuiServlet;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CrawlerGuiActivator extends HttpBundleActivator {

	@Inject
	private CrawlerGuiNavigationEntry crawlerGuiNavigationEntry;

	@Inject
	private CrawlerGuiServlet crawlerGuiServlet;

	public CrawlerGuiActivator() {
		super(CrawlerGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new CrawlerGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<>(super.getServletInfos());
		result.add(new ServletInfo(crawlerGuiServlet, "/"));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationEntry.class, crawlerGuiNavigationEntry));
		return result;
	}

}
