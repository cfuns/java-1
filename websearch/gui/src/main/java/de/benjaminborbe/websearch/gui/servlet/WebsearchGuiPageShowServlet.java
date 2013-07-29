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
import de.benjaminborbe.httpdownloader.api.HttpHeader;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ComparatorUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.websearch.api.WebsearchPage;
import de.benjaminborbe.websearch.api.WebsearchPageIdentifier;
import de.benjaminborbe.websearch.api.WebsearchService;
import de.benjaminborbe.websearch.api.WebsearchServiceException;
import de.benjaminborbe.websearch.gui.WebsearchGuiConstants;
import de.benjaminborbe.websearch.gui.util.WebsearchGuiLinkFactory;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;
import de.benjaminborbe.website.widget.BrWidget;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class WebsearchGuiPageShowServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = -7605803790016397697L;

	private static final String TITLE = "Websearch - Page - Content";

	private final Logger logger;

	private final CalendarUtil calendarUtil;

	private final AuthenticationService authenticationService;

	private final WebsearchService webseachService;

	private final WebsearchGuiLinkFactory websearchGuiLinkFactory;

	private final ComparatorUtil comparatorUtil;

	@Inject
	public WebsearchGuiPageShowServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final NavigationWidget navigationWidget,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final CacheService cacheService,
		final WebsearchService webseachService,
		final WebsearchGuiLinkFactory websearchGuiLinkFactory,
		final ComparatorUtil comparatorUtil
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.calendarUtil = calendarUtil;
		this.authenticationService = authenticationService;
		this.webseachService = webseachService;
		this.websearchGuiLinkFactory = websearchGuiLinkFactory;
		this.comparatorUtil = comparatorUtil;
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

			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final String url = request.getParameter(WebsearchGuiConstants.PARAMETER_PAGE_ID);
			if (url != null) {
				final WebsearchPageIdentifier websearchPageIdentifier = webseachService.createPageIdentifier(url);
				final WebsearchPage websearchPage = webseachService.getPage(sessionIdentifier, websearchPageIdentifier);

				if (websearchPage == null) {
					widgets.add("page " + url + " not found");
					widgets.add(new BrWidget());
				} else {

					{
						widgets.add(new H2Widget("Infos:"));
						final UlWidget ul = new UlWidget();
						ul.add("Url: " + websearchPage.getUrl());
						ul.add("ReturnCode: " + websearchPage.getReturnCode());
						ul.add("LastVisit: " + calendarUtil.toDateTimeString(websearchPage.getLastVisit()));
						ul.add("Duration: " + websearchPage.getDuration());
						widgets.add(ul);
					}
					{
						widgets.add(new H2Widget("Headers:"));
						final HttpHeader header = websearchPage.getHeader();
						if (header == null) {
							widgets.add("-");
						} else {
							final UlWidget ul = new UlWidget();
							for (final String key : comparatorUtil.sort(header.getKeys())) {
								ul.add(key + " = " + StringUtils.join(header.getValues(key), ","));
							}
							widgets.add(ul);
						}
					}
					{
						widgets.add(new H2Widget("Options:"));
						widgets.add(websearchGuiLinkFactory.pageContent(request, websearchPageIdentifier));
						widgets.add(new BrWidget());
						widgets.add(websearchGuiLinkFactory.pageExpire(request, websearchPageIdentifier));
						widgets.add(new BrWidget());
						widgets.add(websearchGuiLinkFactory.pageRefresh(request, websearchPageIdentifier));
						widgets.add(new BrWidget());
					}

				}
			}

			final FormWidget formWidget = new FormWidget();
			formWidget.addFormInputWidget(new FormInputTextWidget(WebsearchGuiConstants.PARAMETER_PAGE_ID).addLabel("Url:"));
			formWidget.addFormInputWidget(new FormInputSubmitWidget("show"));
			widgets.add(formWidget);

			return widgets;
		} catch (WebsearchServiceException e) {
			return new ExceptionWidget(e);
		} catch (AuthenticationServiceException e) {
			return new ExceptionWidget(e);
		}
	}
}
