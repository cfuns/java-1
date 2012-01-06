package de.benjaminborbe.search.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.search.util.SearchServiceComponentRegistry;

@Singleton
public class SearchServiceComponentsServlet extends HttpServlet {

	private static final long serialVersionUID = 7928536214812474981L;

	private static final String TITLE = "SearchServiceComponents";

	private final Logger logger;

	private final SearchServiceComponentRegistry searchServiceComponentRegistry;

	@Inject
	public SearchServiceComponentsServlet(final Logger logger, final SearchServiceComponentRegistry searchServiceComponentRegistry) {
		this.logger = logger;
		this.searchServiceComponentRegistry = searchServiceComponentRegistry;
	}

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		logger.debug("service");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		final PrintWriter out = response.getWriter();
		out.println("<html>");
		printHeader(out);
		printBody(out);
		printFooter(out);
		out.println("</html>");
	}

	protected void printBody(final PrintWriter out) {
		out.println("<h1>" + TITLE + "</h1>");
		final Collection<SearchServiceComponent> cs = searchServiceComponentRegistry.getAll();
		out.println("found " + cs.size() + " SearchServiceComponents<br>");
		out.println("<ul>");
		for (final SearchServiceComponent s : cs) {
			out.println("<li>");
			out.println(s.getClass().getName());
			out.println("</li>");
		}
		out.println("</ul>");
	}

	protected void printHeader(final PrintWriter out) {
		out.println("<head>");
		out.println("<title>" + TITLE + "</title>");
		out.println("</head>");
	}

	protected void printFooter(final PrintWriter out) {
	}

}
