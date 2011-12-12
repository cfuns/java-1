package de.benjaminborbe.search.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchService;

@Singleton
public class SearchServlet extends HttpServlet {

	private static final long serialVersionUID = 7928536214812474981L;

	private final Logger logger;

	private final SearchService searchService;

	private final static String PARAMETER_SEARCH = "q";

	private static final int MAX_RESULTS = 20;

	@Inject
	public SearchServlet(final Logger logger, final SearchService searchService) {
		this.logger = logger;
		this.searchService = searchService;
	}

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException {
		logger.debug("service");
		response.setContentType("application/json");
		final PrintWriter out = response.getWriter();
		final String search = request.getParameter(PARAMETER_SEARCH);

		final List<SearchResult> results = searchService.search(search, MAX_RESULTS);
		out.println("results: " + results.size());
	}
}
