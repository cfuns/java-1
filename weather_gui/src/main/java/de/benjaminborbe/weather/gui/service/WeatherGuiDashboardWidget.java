package de.benjaminborbe.weather.gui.service;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.tools.html.Target;

@Singleton
public class WeatherGuiDashboardWidget implements DashboardContentWidget {

	@Inject
	public WeatherGuiDashboardWidget(final Logger logger) {
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

}
