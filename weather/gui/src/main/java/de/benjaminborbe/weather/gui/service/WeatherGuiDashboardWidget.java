package de.benjaminborbe.weather.gui.service;

import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.weather.gui.WeatherGuiConstants;
import de.benjaminborbe.website.util.Target;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Singleton
public class WeatherGuiDashboardWidget implements DashboardContentWidget {

	@Inject
	public WeatherGuiDashboardWidget() {
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final PrintWriter out = response.getWriter();
		out.println("<a href=\"http://uk.weather.yahoo.com/germany/hesse/wiesbaden-706329/\" target=\"" + Target.BLANK + "\">Yahoo-Weather</a>");
	}

	@Override
	public String getTitle() {
		return "WeatherWidget";
	}

	@Override
	public long getPriority() {
		return 1;
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}

	@Override
	public String getName() {
		return WeatherGuiConstants.NAME;
	}

}
