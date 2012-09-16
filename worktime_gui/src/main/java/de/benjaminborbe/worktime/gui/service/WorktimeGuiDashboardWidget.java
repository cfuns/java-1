package de.benjaminborbe.worktime.gui.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

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
import de.benjaminborbe.worktime.gui.widget.WorktimeListWidget;

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
	public WorktimeGuiDashboardWidget(final Logger logger, final WorktimeService worktimeService, final DateUtil dateUtil, final ParseUtil parseUtil, final CalendarUtil calendarUtil) {
		this.logger = logger;
		this.worktimeService = worktimeService;
		this.dateUtil = dateUtil;
		this.parseUtil = parseUtil;
		this.calendarUtil = calendarUtil;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final int dayAmount = parseUtil.parseInt(request.getParameter(PARAMETER_LIMIT), DEFAULT_DAY_AMOUNT);
		logger.trace("dayAmount = " + dayAmount);
		final ListWidget widgets = new ListWidget();
		try {
			final List<Workday> worktimes = worktimeService.getTimes(dayAmount);
			widgets.add(new WorktimeListWidget(dateUtil, calendarUtil, worktimes));
		}
		catch (final WorktimeServiceException e) {
			widgets.add(new ExceptionWidget(e));
		}
		widgets.render(request, response, context);
	}

	@Override
	public String getTitle() {
		return "Worktime";
	}

	@Override
	public List<JavascriptResource> getJavascriptResource(final HttpServletRequest request, final HttpServletResponse response) {
		final String contextPath = request.getContextPath();
		final List<JavascriptResource> result = new ArrayList<JavascriptResource>();
		result.add(new JavascriptResourceImpl(contextPath + "/js/sorttable.js"));
		return result;
	}

	@Override
	public long getPriority() {
		return 997;
	}
}
