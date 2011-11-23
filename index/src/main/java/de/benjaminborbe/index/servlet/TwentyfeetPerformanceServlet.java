package de.benjaminborbe.index.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.index.service.TwentyFeetPerformanceService;
import de.benjaminborbe.tools.util.HttpDownloadResult;
import de.benjaminborbe.tools.util.MathUtil;

@Singleton
public class TwentyfeetPerformanceServlet extends HttpServlet {

	private static final long serialVersionUID = 5752589798639356721L;

	private static final String TITLE = "Twentyfeet Performance";

	private final Logger logger;

	private final TwentyFeetPerformanceService twentyFeetPerformanceService;

	private final Map<String, List<Long>> durations = new HashMap<String, List<Long>>();

	private final MathUtil mathUtil;

	// only service are allowed to inject
	@Inject
	public TwentyfeetPerformanceServlet(
			final Logger logger,
			final TwentyFeetPerformanceService twentyFeetPerformanceService,
			final MathUtil mathUtil) {
		this.logger = logger;
		this.twentyFeetPerformanceService = twentyFeetPerformanceService;
		this.mathUtil = mathUtil;
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

	private Map<String, Long> getAverageDurations() {
		final Map<String, Long> result = new HashMap<String, Long>();
		for (final Entry<String, List<Long>> e : durations.entrySet()) {
			if (e.getValue().size() == 0) {
				result.put(e.getKey(), -1l);
			}
			else {
				result.put(e.getKey(), mathUtil.avgLong(e.getValue()));
			}
		}
		return result;
	}

	private void printBody(final PrintWriter out) throws IOException {
		out.println("<h1>" + TITLE + "</h1>");
		updatePerformance();

		final Map<String, Long> data = getAverageDurations();

		final List<String> urls = new ArrayList<String>(data.keySet());
		Collections.sort(urls);
		out.println("<table class=\"sortable\">");
		out.println("<tr>");
		out.println("<th>Url</th>");
		out.println("<th class=\"sorttable_numeric\">Duration</th>");
		out.println("<th class=\"sorttable_numeric\">Counter</th>");
		out.println("</tr>");

		for (final String url : urls) {
			final long duration = data.get(url);
			final int counter = durations.containsKey(url) ? durations.get(url).size() : 0;
			out.println("<tr>");
			out.println("<td sorttable_customkey=\"" + url + "\">");
			out.println("<a target=\"_blank\" href=\"" + url + "\">" + url + "</a>");
			out.println("</td>");
			out.println("<td " + (duration == -1 ? "bgcolor=\"red\" " : "") + "sorttable_customkey=\"" + duration + "\">"
					+ duration + " ms</td>");
			out.println("<td sorttable_customkey=\"" + counter + "\">" + counter + "</td>");
			out.println("</tr>");
		}
		out.println("</table>");
	}

	protected synchronized void updatePerformance() throws IOException {
		final Map<URL, HttpDownloadResult> performance = twentyFeetPerformanceService.getPerformance();
		for (final Entry<URL, HttpDownloadResult> e : performance.entrySet()) {
			final String url = e.getKey().toExternalForm();
			List<Long> list;
			if (durations.containsKey(url)) {
				list = durations.get(url);
			}
			else {
				list = new ArrayList<Long>();
				durations.put(url, list);
			}
			if (e.getValue() != null) {
				list.add(e.getValue().getDuration());
			}
		}
	}

	private void printFooter(final PrintWriter out) {
		out.println("</body>");
		out.println("</html>");
	}

}
