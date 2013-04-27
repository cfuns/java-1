package de.benjaminborbe.worktime.gui.service;

import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.RequireJavascriptResource;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.DateUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.JavascriptResourceImpl;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.worktime.api.Workday;
import de.benjaminborbe.worktime.api.WorktimeService;
import de.benjaminborbe.worktime.api.WorktimeServiceException;
import de.benjaminborbe.worktime.gui.WorktimeGuiConstants;
import de.benjaminborbe.worktime.gui.widget.WorktimeListWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class WorktimeGuiDashboardWidget implements DashboardContentWidget, RequireJavascriptResource, Widget {

	private final static int DEFAULT_DAY_AMOUNT = 5;

	private static final String PARAMETER_LIMIT = "limit";

	private final Logger logger;

	private final WorktimeService worktimeService;

	private final DateUtil dateUtil;

	private final ParseUtil parseUtil;

	private final CalendarUtil calendarUtil;

	@Inject
	public WorktimeGuiDashboardWidget(
		final Logger logger,
		final WorktimeService worktimeService,
		final DateUtil dateUtil,
		final ParseUtil parseUtil,
		final CalendarUtil calendarUtil
	) {
		this.logger = logger;
		this.worktimeService = worktimeService;
		this.dateUtil = dateUtil;
		this.parseUtil = parseUtil;
		this.calendarUtil = calendarUtil;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		try {
			final int dayAmount = parseUtil.parseInt(request.getParameter(PARAMETER_LIMIT), DEFAULT_DAY_AMOUNT);
			final ListWidget widgets = new ListWidget();
			logger.trace("dayAmount = " + dayAmount);
			final List<Workday> worktimes = worktimeService.getTimes(dayAmount);
			widgets.add(new WorktimeListWidget(dateUtil, calendarUtil, worktimes));
			widgets.render(request, response, context);
		} catch (final WorktimeServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			widget.render(request, response, context);
		}
	}

	@Override
	public String getTitle() {
		return "Worktime";
	}

	@Override
	public List<JavascriptResource> getJavascriptResource(final HttpServletRequest request, final HttpServletResponse response) {
		final String contextPath = request.getContextPath();
		final List<JavascriptResource> result = new ArrayList<>();
		result.add(new JavascriptResourceImpl(contextPath + "/js/sorttable.js"));
		return result;
	}

	@Override
	public long getPriority() {
		return 997;
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}

	@Override
	public String getName() {
		return WorktimeGuiConstants.NAME;
	}

}
