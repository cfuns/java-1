package de.benjaminborbe.bookmark.gui.servlet;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.website.servlet.WebsiteRedirectServlet;

@Singleton
public class BookmarkGuiDeleteServlet extends WebsiteRedirectServlet {

	private static final long serialVersionUID = 4956434804365230995L;

	private static final String TARGET = "/list";

	@Inject
	public BookmarkGuiDeleteServlet(final Logger logger) {
		super(logger);
	}

	@Override
	protected String getTarget() {
		return TARGET;
	}

}
