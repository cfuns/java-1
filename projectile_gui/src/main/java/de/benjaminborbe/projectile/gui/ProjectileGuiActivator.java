package de.benjaminborbe.projectile.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.projectile.gui.guice.ProjectileGuiModules;
import de.benjaminborbe.projectile.gui.servlet.ProjectileGuiFetchMailReportServlet;
import de.benjaminborbe.projectile.gui.servlet.ProjectileGuiSlacktimeImportServlet;
import de.benjaminborbe.projectile.gui.servlet.ProjectileGuiSlacktimeReportServlet;
import de.benjaminborbe.projectile.gui.servlet.ProjectileGuiSlacktimeServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class ProjectileGuiActivator extends HttpBundleActivator {

	@Inject
	private ProjectileGuiSlacktimeImportServlet projectileGuiSlacktimeImportServlet;

	@Inject
	private ProjectileGuiSlacktimeReportServlet projectileGuiSlacktimeReportServlet;

	@Inject
	private ProjectileGuiSlacktimeServlet projectileGuiSlacktimeServlet;

	@Inject
	private ProjectileGuiFetchMailReportServlet projectileGuiFetchMailReportServlet;

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
		result.add(new ServletInfo(projectileGuiSlacktimeServlet, ProjectileGuiConstants.URL_HOME));
		result.add(new ServletInfo(projectileGuiFetchMailReportServlet, ProjectileGuiConstants.URL_FETCH_REPORT));
		result.add(new ServletInfo(projectileGuiSlacktimeReportServlet, ProjectileGuiConstants.URL_SLACKTIME_REPORT));
		result.add(new ServletInfo(projectileGuiSlacktimeImportServlet, ProjectileGuiConstants.URL_SLACKTIME_IMPORT));
		return result;
	}

}
