package de.benjaminborbe.index.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.index.dao.Bookmark;
import de.benjaminborbe.index.service.BookmarkService;

@Singleton
public class GoServlet extends HttpServlet {

	private static final long serialVersionUID = 4475759115001769671L;

	private static final String PARAMETER_SEARCH = "q";

	private final Logger logger;

	private final BookmarkService bookmarkService;

	// only service are allowed to inject
	@Inject
	public GoServlet(final Logger logger, final BookmarkService bookmarkService) {
		this.logger = logger;
		this.bookmarkService = bookmarkService;
	}

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException {
		logger.debug("service");

		final String search = request.getParameter(PARAMETER_SEARCH);
		for (final Bookmark bookmark : bookmarkService.getBookmarks()) {
			if (bookmark.getName().equalsIgnoreCase(search)) {
				response.sendRedirect(bookmark.getUrl());
				return;
			}
		}
		response.sendRedirect(request.getContextPath());
	}
}
