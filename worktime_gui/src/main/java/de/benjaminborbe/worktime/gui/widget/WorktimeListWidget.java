package de.benjaminborbe.worktime.gui.widget;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.date.DateUtil;
import de.benjaminborbe.worktime.api.Workday;

public class WorktimeListWidget implements Widget {

	private final List<Workday> worktimes;

	private final DateUtil dateUtil;

	public WorktimeListWidget(final DateUtil dateUtil, final List<Workday> worktimes) {
		this.dateUtil = dateUtil;
		this.worktimes = worktimes;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final PrintWriter out = response.getWriter();
		out.println("<table class=\"sortable\">");
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
		for (final Workday workday : worktimes) {
			out.println("<tr>");
			out.println("<td>");
			out.println(dateUtil.dateString(workday.getDate().getTime()));
			out.println("</td>");
			out.println("<td>");
			if (workday.getStart() != null)
				out.println(dateUtil.timeString(workday.getStart().getTime()));
			else
				out.println("-");
			out.println("</td>");
			out.println("<td>");
			if (workday.getEnd() != null)
				out.println(dateUtil.timeString(workday.getEnd().getTime()));
			else
				out.println("-");
			out.println("</td>");
			out.println("</tr>");
		}
		out.println("</table>");
		out.println("<a href=\"" + request.getContextPath() + "/worktime?limit=20\">more</a>");
	}

}
