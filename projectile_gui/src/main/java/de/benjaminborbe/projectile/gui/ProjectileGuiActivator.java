package de.benjaminborbe.projectile.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.projectile.gui.guice.ProjectileGuiModules;
import de.benjaminborbe.projectile.gui.servlet.ProjectileGuiFetchMailReportServlet;
import de.benjaminborbe.projectile.gui.servlet.ProjectileGuiReportAllServlet;
import de.benjaminborbe.projectile.gui.servlet.ProjectileGuiReportImportServlet;
import de.benjaminborbe.projectile.gui.servlet.ProjectileGuiReportJsonServlet;
import de.benjaminborbe.projectile.gui.servlet.ProjectileGuiReportServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class ProjectileGuiActivator extends HttpBundleActivator {

	@Inject
	private ProjectileGuiReportImportServlet projectileGuiSlacktimeImportServlet;

	@Inject
	private ProjectileGuiReportJsonServlet projectileGuiSlacktimeReportServlet;

	@Inject
	private ProjectileGuiReportServlet projectileGuiSlacktimeServlet;

	@Inject
	private ProjectileGuiFetchMailReportServlet projectileGuiFetchMailReportServlet;

	@Inject
	private ProjectileGuiReportAllServlet projectileGuiReportAllServlet;

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
		result.add(new ServletInfo(projectileGuiSlacktimeServlet, ProjectileGuiConstants.URL_REPORT));
		result.add(new ServletInfo(projectileGuiReportAllServlet, ProjectileGuiConstants.URL_REPORT_ALL));
		result.add(new ServletInfo(projectileGuiFetchMailReportServlet, ProjectileGuiConstants.URL_REPORT_FETCH));
		result.add(new ServletInfo(projectileGuiSlacktimeReportServlet, ProjectileGuiConstants.URL_REPORT_JSON));
		result.add(new ServletInfo(projectileGuiSlacktimeImportServlet, ProjectileGuiConstants.URL_REPORT_IMPORT));
		return result;
	}

}
