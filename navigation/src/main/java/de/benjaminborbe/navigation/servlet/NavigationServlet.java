package de.benjaminborbe.navigation.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.html.api.CssResourceRenderer;
import de.benjaminborbe.html.api.JavascriptResourceRenderer;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;

@Singleton
public class NavigationServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Navigation";

	private final NavigationWidget navigationWidget;

	@Inject
	public NavigationServlet(final Logger logger, final CssResourceRenderer cssResourceRenderer, final JavascriptResourceRenderer javascriptResourceRenderer, final NavigationWidget navigationWidget) {
		super(logger, cssResourceRenderer, javascriptResourceRenderer);
		this.navigationWidget = navigationWidget;
	}

	@Override
	protected void printBody(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		final PrintWriter out = response.getWriter();
		out.println("<h2>Navigation</h2>");
		navigationWidget.render(request, response);
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

}
