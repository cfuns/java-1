package de.benjaminborbe.projectile.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.projectile.gui.guice.ProjectileGuiModules;
import de.benjaminborbe.projectile.gui.servlet.ProjectileGuiServlet;
import de.benjaminborbe.projectile.gui.servlet.ProjectileGuiSlacktimeImportServlet;
import de.benjaminborbe.projectile.gui.servlet.ProjectileGuiSlacktimeReportServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class ProjectileGuiActivator extends HttpBundleActivator {

	@Inject
	private ProjectileGuiSlacktimeImportServlet projectileGuiSlacktimeImportServlet;

	@Inject
	private ProjectileGuiSlacktimeReportServlet projectileGuiSlacktimeReportServlet;

	@Inject
	private ProjectileGuiServlet projectileGuiServlet;

	public ProjectileGuiActivator() {
		super(ProjectileGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new ProjectileGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(projectileGuiServlet, ProjectileGuiConstants.URL_HOME));
		result.add(new ServletInfo(projectileGuiSlacktimeReportServlet, ProjectileGuiConstants.URL_SLACKTIME_REPORT));
		result.add(new ServletInfo(projectileGuiSlacktimeImportServlet, ProjectileGuiConstants.URL_SLACKTIME_IMPORT));
		return result;
	}

	// @Override
	// protected Collection<FilterInfo> getFilterInfos() {
	// final Set<FilterInfo> result = new HashSet<FilterInfo>(super.getFilterInfos());
	// result.add(new FilterInfo(projectileFilter, ".*", 998));
	// return result;
	// }

	// @Override
	// protected Collection<ResourceInfo> getResouceInfos() {
	// final Set<ResourceInfo> result = new HashSet<ResourceInfo>(super.getResouceInfos());
	// // result.add(new ResourceInfo("/css", "css"));
	// // result.add(new ResourceInfo("/js", "js"));
	// return result;
	// }
}
