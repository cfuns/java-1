package de.benjaminborbe.sample.servlet;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.html.api.CssResourceRenderer;
import de.benjaminborbe.html.api.JavascriptResourceRenderer;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;

@Singleton
public class SampleServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Sample";

	@Inject
	public SampleServlet(final Logger logger, final CssResourceRenderer cssResourceRenderer, final JavascriptResourceRenderer javascriptResourceRenderer) {
		super(logger, cssResourceRenderer, javascriptResourceRenderer);
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}
}
