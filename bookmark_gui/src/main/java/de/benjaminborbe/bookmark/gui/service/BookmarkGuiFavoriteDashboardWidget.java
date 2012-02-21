package de.benjaminborbe.bookmark.gui.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.bookmark.api.Bookmark;
import de.benjaminborbe.bookmark.api.BookmarkService;
import de.benjaminborbe.bookmark.api.BookmarkServiceException;
import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.tools.html.Target;
import de.benjaminborbe.website.link.LinkRelativWidget;
import de.benjaminborbe.website.link.LinkWidget;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;

@Singleton
public class BookmarkGuiFavoriteDashboardWidget implements DashboardContentWidget {

	private final Logger logger;

	private final BookmarkService bookmarkService;

	private static final Target target = Target.BLANK;

	@Inject
	public BookmarkGuiFavoriteDashboardWidget(final Logger logger, final BookmarkService bookmarkService) {
		this.logger = logger;
		this.bookmarkService = bookmarkService;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		try {
			logger.trace("render");
			final ListWidget widgets = new ListWidget();
			final UlWidget ul = new UlWidget();
			final SessionIdentifier sessionIdentifier = new SessionIdentifier(request);
			for (final Bookmark bookmark : bookmarkService.getBookmarkFavorite(sessionIdentifier)) {
				ul.add(new LinkWidget(buildUrl(bookmark.getUrl()), bookmark.getName()).addTarget(target));
			}
			widgets.add(ul);
			widgets.add(new LinkRelativWidget(request, "/bookmark", "more"));
			widgets.render(request, response, context);
		}
		catch (final BookmarkServiceException e) {
			final ExceptionWidget widget = new ExceptionWidget(e);
			widget.render(request, response, context);
		}
	}

	protected URL buildUrl(final String url) throws MalformedURLException {
		if (url != null && url.indexOf("/") == 0) {
			return new URL("http://bb" + url);
		}
		else {
			return new URL(url);
		}
	}

	@Override
	public String getTitle() {
		return "Bookmark-Favorites";
	}

	@Override
	public long getPriority() {
		return 1;
	}

}
