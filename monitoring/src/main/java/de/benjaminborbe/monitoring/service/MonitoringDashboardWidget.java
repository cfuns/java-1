package de.benjaminborbe.monitoring.service;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.monitoring.api.MonitoringSummaryWidget;

@Singleton
public class MonitoringDashboardWidget implements DashboardContentWidget {

	private static final String TITLE = "Monitoring";

	private final Logger logger;

	private final MonitoringSummaryWidget monitoringSummaryWidget;

	@Inject
	public MonitoringDashboardWidget(final Logger logger, final MonitoringSummaryWidget monitoringSummaryWidget) {
		this.logger = logger;
		this.monitoringSummaryWidget = monitoringSummaryWidget;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		logger.debug("render");
		monitoringSummaryWidget.render(request, response, context);
	}

	@Override
	public String getTitle() {
		return TITLE;
	}

	@Override
	public long getPriority() {
		return 2;
	}

}
