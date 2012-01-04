package de.benjaminborbe.navigation.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.navigation.util.NavigationEntryRegistry;

@Singleton
public class NavigationServlet extends HttpServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private final Logger logger;

	private final NavigationEntryRegistry navigationEntryRegistry;

	@Inject
	public NavigationServlet(final Logger logger, final NavigationEntryRegistry navigationEntryRegistry) {
		this.logger = logger;
		this.navigationEntryRegistry = navigationEntryRegistry;
	}

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException {
		logger.debug("service");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		final PrintWriter out = response.getWriter();
		out.println("<h2>Navigation</h2>");
		out.println("<ul>");
		for (final NavigationEntry navigationEntry : navigationEntryRegistry.getAll()) {
			out.println("<li>");
			out.println("<a href=\"" + buildUrl(request, navigationEntry.getURL()).toExternalForm() + "\">"
					+ navigationEntry.getTitle() + "</a>");
			out.println("</li>");
		}
		out.println("</ul>");
	}

	protected URL buildUrl(final HttpServletRequest request, final String url) throws MalformedURLException {
		if (url != null && url.indexOf("/") == 0) {
			return new URL("http://bb" + url);
		}
		else {
			return new URL(url);
		}
	}
}
