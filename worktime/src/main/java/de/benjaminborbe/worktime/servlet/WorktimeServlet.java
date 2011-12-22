package de.benjaminborbe.worktime.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.worktime.api.Workday;
import de.benjaminborbe.worktime.api.WorktimeService;

@Singleton
public class WorktimeServlet extends HttpServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final int DEFAULT_DAY_AMOUNT = 20;

	private final Logger logger;

	private final WorktimeService worktimeService;

	@Inject
	public WorktimeServlet(final Logger logger, final WorktimeService worktimeService) {
		this.logger = logger;
		this.worktimeService = worktimeService;
	}

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException {
		logger.debug("service");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		final PrintWriter out = response.getWriter();

		printHtml(out);
	}

	protected void printHtml(final PrintWriter out) {
		out.println("<html>");
		printHeader(out);
		try {
			printBody(out);
		}
		catch (final StorageException e) {
			out.println("<body>");
			e.printStackTrace(out);
			out.println("</body>");
		}
		printFooter();
		out.println("</html>");
	}

	protected void printFooter() {

	}

	protected void printBody(final PrintWriter out) throws StorageException {
		out.println("<body>");
		out.println("<h1>Worktime</h1>");
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

		final SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
		final SimpleDateFormat hf = new SimpleDateFormat("hh:mm:ss");
		for (final Workday workday : worktimeService.getTimes(DEFAULT_DAY_AMOUNT)) {
			out.println("<tr>");
			out.println("<td>");
			out.println(df.format(workday.getDate().getTime()));
			out.println("</td>");
			out.println("<td>");
			if (workday.getStart() != null)
				out.println(hf.format(workday.getStart().getTime()));
			else
				out.println("-");
			out.println("</td>");
			out.println("<td>");
			if (workday.getEnd() != null)
				out.println(hf.format(workday.getEnd().getTime()));
			else
				out.println("-");
			out.println("</td>");
			out.println("</tr>");
		}
		out.println("</table>");
		out.println("</body>");
	}

	protected void printHeader(final PrintWriter out) {
		out.println("<head>");
		out.println("<script type=\"text/javascript\" src=\"sorttable.js\"></script>");
		out.println("</head>");
	}
}
