package de.benjaminborbe.projectile.gui;

import com.google.inject.Inject;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.projectile.gui.guice.ProjectileGuiModules;
import de.benjaminborbe.projectile.gui.service.ProjectileGuiNavigationEntry;
import de.benjaminborbe.projectile.gui.servlet.ProjectileGuiReportFetchMailServlet;
import de.benjaminborbe.projectile.gui.servlet.ProjectileGuiReportImportServlet;
import de.benjaminborbe.projectile.gui.servlet.ProjectileGuiReportJsonServlet;
import de.benjaminborbe.projectile.gui.servlet.ProjectileGuiReportTeamAllServlet;
import de.benjaminborbe.projectile.gui.servlet.ProjectileGuiReportTeamCurrentServlet;
import de.benjaminborbe.projectile.gui.servlet.ProjectileGuiReportUserAllServlet;
import de.benjaminborbe.projectile.gui.servlet.ProjectileGuiReportUserCurrentServlet;
import de.benjaminborbe.projectile.gui.servlet.ProjectileGuiServlet;
import de.benjaminborbe.projectile.gui.servlet.ProjectileGuiTeamCreateServlet;
import de.benjaminborbe.projectile.gui.servlet.ProjectileGuiTeamDeleteServlet;
import de.benjaminborbe.projectile.gui.servlet.ProjectileGuiTeamListServlet;
import de.benjaminborbe.projectile.gui.servlet.ProjectileGuiTeamUpdateServlet;
import de.benjaminborbe.projectile.gui.servlet.ProjectileGuiTeamUserRemoveServlet;
import de.benjaminborbe.projectile.gui.servlet.ProjectileGuiTeamViewServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ProjectileGuiActivator extends HttpBundleActivator {

	@Inject
	private ProjectileGuiNavigationEntry projectileGuiNavigationEntry;

	@Inject
	private ProjectileGuiTeamUserRemoveServlet projectileGuiTeamUserRemoveServlet;

	@Inject
	private ProjectileGuiTeamViewServlet projectileGuiTeamViewServlet;

	@Inject
	private ProjectileGuiServlet projectileGuiServlet;

	@Inject
	private ProjectileGuiReportTeamAllServlet projectileGuiReportTeamAllServlet;

	@Inject
	private ProjectileGuiReportTeamCurrentServlet projectileGuiReportTeamCurrentServlet;

	@Inject
	private ProjectileGuiTeamCreateServlet projectileGuiTeamCreateServlet;

	@Inject
	private ProjectileGuiTeamDeleteServlet projectileGuiTeamDeleteServlet;

	@Inject
	private ProjectileGuiTeamListServlet projectileGuiTeamListServlet;

	@Inject
	private ProjectileGuiTeamUpdateServlet projectileGuiTeamUpdateServlet;

	@Inject
	private ProjectileGuiReportImportServlet projectileGuiSlacktimeImportServlet;

	@Inject
	private ProjectileGuiReportJsonServlet projectileGuiSlacktimeReportServlet;

	@Inject
	private ProjectileGuiReportUserCurrentServlet projectileGuiSlacktimeServlet;

	@Inject
	private ProjectileGuiReportFetchMailServlet projectileGuiFetchMailReportServlet;

	@Inject
	private ProjectileGuiReportUserAllServlet projectileGuiReportAllServlet;

	public ProjectileGuiActivator() {
		super(ProjectileGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new ProjectileGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<>(super.getServletInfos());
		result.add(new ServletInfo(projectileGuiTeamViewServlet, ProjectileGuiConstants.URL_TEAM_VIEW));
		result.add(new ServletInfo(projectileGuiServlet, ProjectileGuiConstants.URL_HOME));
		result.add(new ServletInfo(projectileGuiSlacktimeServlet, ProjectileGuiConstants.URL_REPORT_USER_CURRENT));
		result.add(new ServletInfo(projectileGuiReportAllServlet, ProjectileGuiConstants.URL_REPORT_USER_ALL));
		result.add(new ServletInfo(projectileGuiFetchMailReportServlet, ProjectileGuiConstants.URL_REPORT_FETCH));
		result.add(new ServletInfo(projectileGuiSlacktimeReportServlet, ProjectileGuiConstants.URL_REPORT_JSON));
		result.add(new ServletInfo(projectileGuiSlacktimeImportServlet, ProjectileGuiConstants.URL_REPORT_IMPORT));
		result.add(new ServletInfo(projectileGuiReportTeamAllServlet, ProjectileGuiConstants.URL_REPORT_TEAM_ALL));
		result.add(new ServletInfo(projectileGuiReportTeamCurrentServlet, ProjectileGuiConstants.URL_REPORT_TEAM_CURRENT));
		result.add(new ServletInfo(projectileGuiTeamCreateServlet, ProjectileGuiConstants.URL_TEAM_CREATE));
		result.add(new ServletInfo(projectileGuiTeamDeleteServlet, ProjectileGuiConstants.URL_TEAM_DELETE));
		result.add(new ServletInfo(projectileGuiTeamListServlet, ProjectileGuiConstants.URL_TEAM_LIST));
		result.add(new ServletInfo(projectileGuiTeamUpdateServlet, ProjectileGuiConstants.URL_TEAM_UPDATE));
		result.add(new ServletInfo(projectileGuiTeamUserRemoveServlet, ProjectileGuiConstants.URL_TEAM_USER_REMOVE));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationEntry.class, projectileGuiNavigationEntry));
		return result;
	}
}
