package de.benjaminborbe.slash.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.slash.gui.guice.SlashGuiModules;
import de.benjaminborbe.slash.gui.servlet.SlashGuiLogFilter;
import de.benjaminborbe.slash.gui.servlet.SlashGuiRobotsTxtServlet;
import de.benjaminborbe.slash.gui.servlet.SlashGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.FilterInfo;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class SlashGuiActivator extends HttpBundleActivator {

	@Inject
	private SlashGuiServlet slashServlet;

	@Inject
	private SlashGuiLogFilter slashLogFilter;

	@Inject
	private SlashGuiRobotsTxtServlet robotsTxtServlet;

	public SlashGuiActivator() {
		super("");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new SlashGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(slashServlet, "/"));
		result.add(new ServletInfo(robotsTxtServlet, "/robots.txt"));
		return result;
	}

	@Override
	protected Collection<FilterInfo> getFilterInfos() {
		final Set<FilterInfo> result = new HashSet<FilterInfo>(super.getFilterInfos());
		result.add(new FilterInfo(slashLogFilter, ".*", 1));
		return result;
	}

	@Override
	protected Collection<ResourceInfo> getResouceInfos() {
		final Set<ResourceInfo> result = new HashSet<ResourceInfo>(super.getResouceInfos());
		result.add(new ResourceInfo("/css", "css"));
		result.add(new ResourceInfo("/js", "js"));
		result.add(new ResourceInfo("/images", "images"));
		return result;
	}

}
