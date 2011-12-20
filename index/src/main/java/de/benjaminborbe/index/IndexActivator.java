package de.benjaminborbe.index;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.index.guice.IndexModules;
import de.benjaminborbe.index.servlet.IndexServlet;
import de.benjaminborbe.index.servlet.RobotsTxtServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class IndexActivator extends HttpBundleActivator {

	@Inject
	private IndexServlet indexServlet;

	@Inject
	private RobotsTxtServlet robotsTxtServlet;

	public IndexActivator() {
		super("index");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new IndexModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(indexServlet, "/"));
		result.add(new ServletInfo(robotsTxtServlet, "/robots.txt"));
		return result;
	}

	@Override
	protected Collection<ResourceInfo> getResouceInfos() {
		final Set<ResourceInfo> result = new HashSet<ResourceInfo>(super.getResouceInfos());
		result.add(new ResourceInfo("/js", "js"));
		return result;
	}

}
