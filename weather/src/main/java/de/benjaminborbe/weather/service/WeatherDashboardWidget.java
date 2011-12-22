package de.benjaminborbe.weather.service;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.dashboard.api.DashboardWidget;

@Singleton
public class WeatherDashboardWidget implements DashboardWidget {

	@Inject
	public WeatherDashboardWidget(final Logger logger) {
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		final PrintWriter out = response.getWriter();
		out.println("weather");
	}

	@Override
	public String getTitle() {
		return "WeatherWidget";
	}
}
