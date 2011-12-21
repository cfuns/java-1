package de.benjaminborbe.search.service;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.dashboard.api.DashboardWidget;

@Singleton
public class SearchDashboardWidget implements DashboardWidget {

	private static final String TITLE = "SearchDashboardWidget";

	private final Logger logger;

	@Inject
	public SearchDashboardWidget(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		logger.debug("render");

		final PrintWriter out = response.getWriter();
		out.println("SearchDashboard");
	}

	@Override
	public String getTitle() {
		return TITLE;
	}

}
