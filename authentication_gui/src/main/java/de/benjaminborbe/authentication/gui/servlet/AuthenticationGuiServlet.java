package de.benjaminborbe.authentication.gui.servlet;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.website.servlet.WebsiteRedirectServlet;

@Singleton
public class AuthenticationGuiServlet extends WebsiteRedirectServlet {

	private static final long serialVersionUID = -4538727884647259439L;

	private static final String TARGET = "bookmark/list";

	@Inject
	public AuthenticationGuiServlet(final Logger logger) {
		super(logger);
	}

	@Override
	protected String getTarget() {
		return TARGET;
	}

}
