package de.benjaminborbe.portfolio.gui;

import de.benjaminborbe.portfolio.gui.guice.PortfolioGuiModules;
import de.benjaminborbe.portfolio.gui.servlet.PortfolioGuiCacheFilter;
import de.benjaminborbe.portfolio.gui.servlet.PortfolioGuiContactServlet;
import de.benjaminborbe.portfolio.gui.servlet.PortfolioGuiGalleryServlet;
import de.benjaminborbe.portfolio.gui.servlet.PortfolioGuiImageServlet;
import de.benjaminborbe.portfolio.gui.servlet.PortfolioGuiLinksServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.FilterInfo;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PortfolioGuiActivator extends HttpBundleActivator {

	@Inject
	private PortfolioGuiGalleryServlet portfolioGuiGalleryServlet;

	@Inject
	private PortfolioGuiContactServlet portfolioGuiContactServlet;

	@Inject
	private PortfolioGuiLinksServlet portfolioGuiLinksServlet;

	@Inject
	private PortfolioGuiImageServlet portfolioGuiImageServlet;

	@Inject
	private PortfolioGuiCacheFilter portfolioCacheFilter;

	public PortfolioGuiActivator() {
		super(PortfolioGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new PortfolioGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<>(super.getServletInfos());
		result.add(new ServletInfo(portfolioGuiGalleryServlet, PortfolioGuiConstants.URL_GALLERY));
		result.add(new ServletInfo(portfolioGuiContactServlet, PortfolioGuiConstants.URL_CONTACT));
		result.add(new ServletInfo(portfolioGuiImageServlet, PortfolioGuiConstants.URL_IMAGE));
		result.add(new ServletInfo(portfolioGuiLinksServlet, PortfolioGuiConstants.URL_LINKS));
		return result;
	}

	@Override
	protected Collection<FilterInfo> getFilterInfos() {
		final Set<FilterInfo> result = new HashSet<>(super.getFilterInfos());
		result.add(new FilterInfo(portfolioCacheFilter, ".*", 500));
		return result;
	}

	@Override
	protected Collection<ResourceInfo> getResouceInfos() {
		final Set<ResourceInfo> result = new HashSet<>(super.getResouceInfos());
		result.add(new ResourceInfo("/css", "css"));
		result.add(new ResourceInfo("/js", "js"));
		result.add(new ResourceInfo("/images", "images"));
		return result;
	}
}
