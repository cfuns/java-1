package de.benjaminborbe.projectile.gui.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;

import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.projectile.api.TeamIdentifier;
import de.benjaminborbe.projectile.gui.ProjectileGuiConstants;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;

public class ProjectileLinkFactory {

	private final UrlUtil urlUtil;

	@Inject
	public ProjectileLinkFactory(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	public Widget updateTeam(final HttpServletRequest request, final TeamIdentifier id) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + ProjectileGuiConstants.NAME + ProjectileGuiConstants.URL_TEAM_UPDATE, new MapParameter().add(
				ProjectileGuiConstants.PARAMETER_TEAM_ID, String.valueOf(id)), "update");
	}

	public Widget deleteTeam(final HttpServletRequest request, final TeamIdentifier id) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + ProjectileGuiConstants.NAME + ProjectileGuiConstants.URL_TEAM_DELETE, new MapParameter().add(
				ProjectileGuiConstants.PARAMETER_TEAM_ID, String.valueOf(id)), "delete");
	}

	public Widget createTeam(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + ProjectileGuiConstants.NAME + ProjectileGuiConstants.URL_TEAM_CREATE, "create");
	}

	public Widget teamList(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + ProjectileGuiConstants.NAME + ProjectileGuiConstants.URL_TEAM_LIST, "list");
	}

	public Widget reportForCurrentUser(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + ProjectileGuiConstants.NAME + ProjectileGuiConstants.URL_REPORT_USER_CURRENT, "user-report");
	}

	public Widget reportForCurrentTeam(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + ProjectileGuiConstants.NAME + ProjectileGuiConstants.URL_REPORT_TEAM_CURRENT, "team-report");
	}

	public Widget reportForAllUser(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + ProjectileGuiConstants.NAME + ProjectileGuiConstants.URL_REPORT_USER_ALL, "users-report");
	}

	public Widget reportForAllTeam(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + ProjectileGuiConstants.NAME + ProjectileGuiConstants.URL_REPORT_TEAM_ALL, "teams-report");
	}

	public Widget fetchReport(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + ProjectileGuiConstants.NAME + ProjectileGuiConstants.URL_REPORT_FETCH, "fetch-report");
	}

	public Widget importReport(final HttpServletRequest request) {
		return null;
	}

}
