package de.benjaminborbe.websearch.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.httpdownloader.api.HttpUtil;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.websearch.api.WebsearchPage;
import de.benjaminborbe.websearch.api.WebsearchPageIdentifier;
import de.benjaminborbe.websearch.api.WebsearchService;
import de.benjaminborbe.websearch.api.WebsearchServiceException;
import de.benjaminborbe.websearch.gui.WebsearchGuiConstants;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.PreWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class WebsearchGuiPageContentServlet extends WebsiteHtmlServlet {

	private static final String TITLE = "Websearch - Page - Content";

	private final Logger logger;

	private final HttpUtil httpUtil;

	private final AuthenticationService authenticationService;

	private final WebsearchService webseachService;

	@Inject
	public WebsearchGuiPageContentServlet(
		final Logger logger,
		final HttpUtil httpUtil,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final NavigationWidget navigationWidget,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final CacheService cacheService,
		final WebsearchService webseachService
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.httpUtil = httpUtil;
		this.authenticationService = authenticationService;
		this.webseachService = webseachService;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(
		final HttpServletRequest request,
		final HttpServletResponse response,
		final HttpContext context
	) throws IOException, PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

			final String url = request.getParameter(WebsearchGuiConstants.PARAMETER_PAGE_ID);
			if (url != null) {
				final WebsearchPageIdentifier websearchPageIdentifier = webseachService.createPageIdentifier(url);
				final WebsearchPage websearchPage = webseachService.getPage(sessionIdentifier, websearchPageIdentifier);
				final String content = httpUtil.getContent(websearchPage);
				widgets.add(new PreWidget(content));
			}

			final FormWidget formWidget = new FormWidget();
			formWidget.addFormInputWidget(new FormInputTextWidget(WebsearchGuiConstants.PARAMETER_PAGE_ID).addLabel("Url:"));
			formWidget.addFormInputWidget(new FormInputSubmitWidget("show"));
			widgets.add(formWidget);

			return widgets;
		} catch (WebsearchServiceException | AuthenticationServiceException e) {
			return new ExceptionWidget(e);
		}
	}
}
