package de.benjaminborbe.index.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class IndexServlet extends HttpServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String PAGE_TITLE = "BB Dashboard";

	private final Logger logger;

	@Inject
	public IndexServlet(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException {
		logger.debug("service");
		response.setContentType("text/html");
		final PrintWriter out = response.getWriter();
		printHtml(out);
	}

	protected void printHtml(final PrintWriter out) {
		out.println("<html>");
		printHead(out);
		printBody(out);
		out.println("</html>");
	}

	protected void printHead(final PrintWriter out) {
		out.println("<head>");
		out.println("<title>" + PAGE_TITLE + "</title>");
		out.println("</head>");
	}

	protected void printBody(final PrintWriter out) {
		out.println("<body>");
		out.println("<h1>" + PAGE_TITLE + "</h1>");
		printLinks(out);
		out.println("</body>");
	}

	protected void printLinks(final PrintWriter out) {
		out.println("<h2>Links</h2>");
		final Map<String, String> links = getLinks();
		for (final Entry<String, String> e : links.entrySet()) {
			out.println("<li>");
			out.println("<a href=\"" + e.getKey() + "\" target=\"_blank\">" + e.getValue() + "</a>");
			out.println("</li>");
		}
	}

	protected Map<String, String> getLinks() {
		final Map<String, String> links = new TreeMap<String, String>();
		links.put("http://localhost:8180/app/", "Devel - Twentyfeet");
		links.put("http://127.0.0.1:8888/app/Home.html?gwt.codesvr=127.0.0.1:9997", "Devel - Twentyfeet - GWT");
		links.put("https://timetracker.rp.seibert-media.net/", "Live - Timetracker");
		links.put("https://www.twentyfeet.com/", "Live - Twentyfeet");
		return links;
	}
}
