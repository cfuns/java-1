package de.benjaminborbe.worktime.gui.widget;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.DateUtil;
import de.benjaminborbe.worktime.api.Workday;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class WorktimeListWidget implements Widget {

	private final List<Workday> worktimes;

	private final DateUtil dateUtil;

	private final CalendarUtil calendarUtil;

	public WorktimeListWidget(final DateUtil dateUtil, final CalendarUtil calendarUtil, final List<Workday> worktimes) {
		this.dateUtil = dateUtil;
		this.calendarUtil = calendarUtil;
		this.worktimes = worktimes;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final PrintWriter out = response.getWriter();
		out.println("<table class=\"sortable\">");
		printHead(out);
		for (final Workday workday : worktimes) {
			printWorktime(out, workday);
		}
		out.println("</table>");
		out.println("<a href=\"" + request.getContextPath() + "/worktime?limit=20\">more</a>");
	}

	protected void printHead(final PrintWriter out) {
		out.println("<tr>");
		out.println("<th>");
		out.println("Day");
		out.println("</th>");
		out.println("<th>");
		out.println("StartTime");
		out.println("</th>");
		out.println("<th>");
		out.println("EndTime");
		out.println("</th>");
		out.println("</tr>");
	}

	protected void printWorktime(final PrintWriter out, final Workday workday) {

		final boolean isToday = isToday(workday);

		out.println("<tr>");
		out.println("<td>");
		if (isToday) {
			out.println("<b>");
		}
		out.println(dateUtil.dateString(workday.getDate().getTime()));
		if (isToday) {
			out.println("</b>");
		}
		out.println("</td>");
		out.println("<td>");
		if (isToday) {
			out.println("<b>");
		}
		if (workday.getStart() != null) {
			out.println(dateUtil.timeString(workday.getStart().getTime()));
		} else {
			out.println("-");
		}
		if (isToday) {
			out.println("</b>");
		}
		out.println("</td>");
		out.println("<td>");
		if (isToday) {
			out.println("<b>");
		}
		if (workday.getEnd() != null) {
			out.println(dateUtil.timeString(workday.getEnd().getTime()));
		} else {
			out.println("-");
		}
		if (isToday) {
			out.println("</b>");
		}
		out.println("</td>");
		out.println("</tr>");
	}

	protected boolean isToday(final Workday workday) {
		return calendarUtil.dayEquals(workday.getDate(), calendarUtil.now());
	}
}
