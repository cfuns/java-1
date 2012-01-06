package de.benjaminborbe.index.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.html.api.CssResourceRenderer;
import de.benjaminborbe.html.api.JavascriptResourceRenderer;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;

@Singleton
public class IndexServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Index";

	@Inject
	public IndexServlet(final Logger logger, final CssResourceRenderer cssResourceRenderer, final JavascriptResourceRenderer javascriptResourceRenderer) {
		super(logger, cssResourceRenderer, javascriptResourceRenderer);
	}

	@Override
	protected void printBody(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		logger.debug("service");
		final PrintWriter out = response.getWriter();
		out.println("Index");
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}
}
