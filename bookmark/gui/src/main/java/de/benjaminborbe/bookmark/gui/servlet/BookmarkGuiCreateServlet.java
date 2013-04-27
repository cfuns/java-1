package de.benjaminborbe.bookmark.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.bookmark.api.BookmarkService;
import de.benjaminborbe.bookmark.api.BookmarkServiceException;
import de.benjaminborbe.bookmark.gui.util.BookmarkGuiKeywordUtil;
import de.benjaminborbe.bookmark.gui.util.BookmarkGuiLinkFactory;
import de.benjaminborbe.bookmark.gui.util.BookmarkGuiWebsiteHtmlServlet;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormMethod;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.ValidationExceptionWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class BookmarkGuiCreateServlet extends BookmarkGuiWebsiteHtmlServlet {

	private static final String PARAMETER_KEYWORDS = "keywords";

	private static final String PARAMETER_DESCRIPTION = "description";

	private static final String PARAMETER_NAME = "name";

	private static final String PARAMETER_URL = "url";

	private static final long serialVersionUID = 4468520728605522219L;

	private static final String TITLE = "Bookmark - Create";

	private final BookmarkService bookmarkService;

	private final Logger logger;

	private final AuthenticationService authenticationService;

	private final BookmarkGuiKeywordUtil bookmarkGuiKeywordUtil;

	private final BookmarkGuiLinkFactory bookmarkGuiLinkFactory;

	@Inject
	public BookmarkGuiCreateServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final AuthenticationService authenticationService,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final BookmarkService bookmarkService,
		final UrlUtil urlUtil,
		final BookmarkGuiKeywordUtil bookmarkGuiKeywordUtil,
		final AuthorizationService authorizationService,
		final BookmarkGuiLinkFactory bookmarkGuiLinkFactory,
		final CacheService cacheService
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.bookmarkService = bookmarkService;
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.bookmarkGuiKeywordUtil = bookmarkGuiKeywordUtil;
		this.bookmarkGuiLinkFactory = bookmarkGuiLinkFactory;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
		PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final String url = request.getParameter(PARAMETER_URL);
			final String name = request.getParameter(PARAMETER_NAME);
			final String description = request.getParameter(PARAMETER_DESCRIPTION);
			final String keywords = request.getParameter(PARAMETER_KEYWORDS);
			if (url != null && name != null && description != null && keywords != null) {
				final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
				try {
					bookmarkService.createBookmark(sessionIdentifier, url, name, description, bookmarkGuiKeywordUtil.buildKeywords(keywords));
					throw new RedirectException(request.getContextPath() + "/bookmark/list");
				} catch (final ValidationException e) {
					widgets.add("add bookmark failed!");
					widgets.add(new ValidationExceptionWidget(e));
				}
			}
			final FormWidget formWidget = new FormWidget().addMethod(FormMethod.POST);
			formWidget.addFormInputWidget(new FormInputTextWidget(PARAMETER_URL).addLabel("Url").addPlaceholder("url..."));
			formWidget.addFormInputWidget(new FormInputTextWidget(PARAMETER_NAME).addLabel("Name").addPlaceholder("name..."));
			formWidget.addFormInputWidget(new FormInputTextWidget(PARAMETER_DESCRIPTION).addLabel("Description").addPlaceholder("description..."));
			formWidget.addFormInputWidget(new FormInputTextWidget(PARAMETER_KEYWORDS).addLabel("Keywords").addPlaceholder("keywords..."));
			formWidget.addFormInputWidget(new FormInputSubmitWidget("create"));
			widgets.add(formWidget);

			widgets.add(bookmarkGuiLinkFactory.listBookmarks(request));

			return widgets;
		} catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		} catch (final BookmarkServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}
}
