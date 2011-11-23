package de.benjaminborbe.index.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.index.service.TwentyFeetPerformanceService;
import de.benjaminborbe.index.util.HttpDownloadResult;

@Singleton
public class TwentyfeetPerformanceServlet extends HttpServlet {

	private static final long serialVersionUID = 5752589798639356721L;

	private static final String TITLE = "Twentyfeet Performance";

	private final Logger logger;

	private final TwentyFeetPerformanceService twentyFeetPerformanceService;

	// only service are allowed to inject
	@Inject
	public TwentyfeetPerformanceServlet(
			final Logger logger,
			final TwentyFeetPerformanceService twentyFeetPerformanceService) {
		this.logger = logger;
		this.twentyFeetPerformanceService = twentyFeetPerformanceService;
	}

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException {
		logger.debug("service");
		response.setContentType("text/html");
		final PrintWriter out = response.getWriter();
		final String contextPath = request.getContextPath();
		printHeader(out, contextPath);
		printBody(out);
		printFooter(out);

	}

	private void printHeader(final PrintWriter out, final String contextPath) {
		out.println("<html>");
		out.println("<head>");
		out.println("<title>" + TITLE + "</title>");
		out.println("<script src=\"" + contextPath + "/js/sorttable.js\"></script>");
		out.println("</head>");
		out.println("<body>");
	}

	private void printBody(final PrintWriter out) throws IOException {
		out.println("<h1>" + TITLE + "</h1>");
		final Map<URL, HttpDownloadResult> data = twentyFeetPerformanceService.getPerformance();
		final List<URL> urls = new ArrayList<URL>(data.keySet());
		Collections.sort(urls, new Comparator<URL>() {

			@Override
			public int compare(final URL a, final URL b) {
				return a.toString().compareTo(b.toString());
			}
		});

		out.println("<table class=\"sortable\">");
		out.println("<tr>");
		out.println("<th>Url</th>");
		out.println("<th class=\"sorttable_numeric\">Duration</th>");
		out.println("</tr>");

		for (final URL url : urls) {
			final long duration = data.get(url).getDuration();
			out.println("<tr>");
			out.println("<td sorttable_customkey=\"" + url + "\">");
			out.println("<a target=\"_blank\" href=\"" + url + "\">" + url + "</a>");
			out.println("</td>");
			out.println("<td sorttable_customkey=\"" + duration + "\">" + duration + " ms</td>");
			out.println("</tr>");
		}
		out.println("</table>");
	}

	private void printFooter(final PrintWriter out) {
		out.println("</body>");
		out.println("</html>");
	}

}
