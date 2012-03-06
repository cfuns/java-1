package de.benjaminborbe.dhl.gui.servlet;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.website.servlet.WebsiteRedirectServlet;

@Singleton
public class DhlGuiServlet extends WebsiteRedirectServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	@Inject
	public DhlGuiServlet(final Logger logger) {
		super(logger);
	}

	@Override
	protected String getTarget() {
		return "/dhl/list";
	}
}
