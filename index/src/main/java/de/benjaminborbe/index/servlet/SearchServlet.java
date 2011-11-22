package de.benjaminborbe.index.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.index.dao.Bookmark;
import de.benjaminborbe.index.service.BookmarkService;

@Singleton
public class SearchServlet extends HttpServlet {

	private static final long serialVersionUID = 7928536214812474981L;

	private final Logger logger;

	private final BookmarkService bookmarkService;

	private final static String PARAMETER_SEARCH = "term";

	@Inject
	public SearchServlet(final Logger logger, final BookmarkService bookmarkService) {
		this.logger = logger;
		this.bookmarkService = bookmarkService;
	}

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException {
		logger.debug("service");
		logger.debug(request.getParameterMap().keySet().toString());
		response.setContentType("application/json");
		final PrintWriter out = response.getWriter();
		final String search = request.getParameter(PARAMETER_SEARCH);
		// final List<Bookmark> bookmarks = bookmarkService.getBookmarks();
		final List<Bookmark> bookmarks = bookmarkService.searchBookmarks(search);
		logger.debug("found " + bookmarks.size() + " bookmarks");
		final JSONArray obj = buildJson(bookmarks);
		obj.writeJSONString(out);
	}

	@SuppressWarnings("unchecked")
	protected JSONArray buildJson(final List<Bookmark> bookmarks) {
		final JSONArray result = new JSONArray();
		for (final Bookmark bookmark : bookmarks) {
			// final JSONObject bookmarkJson = new JSONObject();
			// bookmarkJson.put("id", new Long(bookmark.getId()));
			// bookmarkJson.put("text", bookmark.getName());
			// result.add(bookmarkJson);
			result.add(bookmark.getName());
		}
		return result;
	}
}
