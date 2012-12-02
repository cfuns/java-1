package de.benjaminborbe.monitoring.gui.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.RequireCssResource;
import de.benjaminborbe.monitoring.api.MonitoringSummaryWidget;
import de.benjaminborbe.monitoring.gui.MonitoringGuiConstants;

@Singleton
public class MonitoringGuiDashboardWidget implements DashboardContentWidget, RequireCssResource {

	private static final String TITLE = "Monitoring";

	private final Logger logger;

	private final MonitoringSummaryWidget monitoringSummaryWidget;

	@Inject
	public MonitoringGuiDashboardWidget(final Logger logger, final MonitoringSummaryWidget monitoringSummaryWidget) {
		this.logger = logger;
		this.monitoringSummaryWidget = monitoringSummaryWidget;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		logger.trace("render");
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

	@Override
	public List<CssResource> getCssResource(final HttpServletRequest request, final HttpServletResponse response) {
		return monitoringSummaryWidget.getCssResource(request, response);
	}

	@Override
	public boolean isAdminRequired() {
		return true;
	}

	@Override
	public String getName() {
		return MonitoringGuiConstants.NAME;
	}

}
