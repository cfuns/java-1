package de.benjaminborbe.portfolio.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.portfolio.gui.guice.PortfolioGuiModules;
import de.benjaminborbe.portfolio.gui.servlet.PortfolioGuiContactServlet;
import de.benjaminborbe.portfolio.gui.servlet.PortfolioGuiLinksServlet;
import de.benjaminborbe.portfolio.gui.servlet.PortfolioGuiGalleryServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class PortfolioGuiActivator extends HttpBundleActivator {

	@Inject
	private PortfolioGuiGalleryServlet portfolioGuiServlet;

	@Inject
	private PortfolioGuiContactServlet portfolioGuiContactServlet;

	@Inject
	private PortfolioGuiLinksServlet portfolioGuiLinksServlet;

	public PortfolioGuiActivator() {
		super(PortfolioGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new PortfolioGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(portfolioGuiServlet, PortfolioGuiConstants.URL_HOME));
		result.add(new ServletInfo(portfolioGuiContactServlet, PortfolioGuiConstants.URL_CONTACT));
		result.add(new ServletInfo(portfolioGuiLinksServlet, PortfolioGuiConstants.URL_LINKS));
		return result;
	}

	// @Override
	// protected Collection<FilterInfo> getFilterInfos() {
	// final Set<FilterInfo> result = new HashSet<FilterInfo>(super.getFilterInfos());
	// result.add(new FilterInfo(portfolioFilter, ".*", 998));
	// return result;
	// }

	@Override
	protected Collection<ResourceInfo> getResouceInfos() {
		final Set<ResourceInfo> result = new HashSet<ResourceInfo>(super.getResouceInfos());
		result.add(new ResourceInfo("/css", "css"));
		result.add(new ResourceInfo("/js", "js"));
		result.add(new ResourceInfo("/images", "images"));
		return result;
	}
}
