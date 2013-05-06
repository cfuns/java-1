package de.benjaminborbe.crawler.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.crawler.api.CrawlerException;
import de.benjaminborbe.crawler.api.CrawlerInstruction;
import de.benjaminborbe.crawler.api.CrawlerInstructionBuilder;
import de.benjaminborbe.crawler.api.CrawlerService;
import de.benjaminborbe.crawler.gui.CrawlerGuiConstants;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormMethod;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class CrawlerGuiServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Crawler";

	private static final int TIMEOUT = 5000;

	public static final int DEFAULT_DEPTH = 0;

	private final ParseUtil parseUtil;

	private final CrawlerService crawlerService;

	private final Logger logger;

	@Inject
	public CrawlerGuiServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final NavigationWidget navigationWidget,
		final AuthenticationService authenticationService,
		final Provider<HttpContext> httpContextProvider,
		final CrawlerService crawlerService,
		final UrlUtil urlUtil,
		final AuthorizationService authorizationService,
		final CacheService cacheService
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.parseUtil = parseUtil;
		this.crawlerService = crawlerService;
		this.logger = logger;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
		PermissionDeniedException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));
			final String url = request.getParameter(CrawlerGuiConstants.PARAMETER_URL);
			final String depth = request.getParameter(CrawlerGuiConstants.PARAMETER_DEPTH);
			if (url != null && depth != null) {
				try {
					final CrawlerInstruction crawlerInstructionBuilder = new CrawlerInstructionBuilder(parseUtil.parseURL(url), parseUtil.parseLong(depth, DEFAULT_DEPTH), TIMEOUT);
					crawlerService.processCrawlerInstruction(crawlerInstructionBuilder);
					widgets.add("add url " + url + " successful");
				} catch (final CrawlerException e) {
					logger.debug(e.getClass().getName(), e);
					widgets.add("add url " + url + " failed");
				}
			} else {
				final String action = request.getContextPath() + "/crawler";
				final FormWidget formWidget = new FormWidget(action).addMethod(FormMethod.POST);
				formWidget.addFormInputWidget(new FormInputTextWidget(CrawlerGuiConstants.PARAMETER_URL).addPlaceholder("url...").addLabel("Url:"));
				formWidget.addFormInputWidget(new FormInputTextWidget(CrawlerGuiConstants.PARAMETER_DEPTH).addPlaceholder(String.valueOf(DEFAULT_DEPTH)).addLabel("Depth:"));
				formWidget.addFormInputWidget(new FormInputSubmitWidget("crawle"));
				widgets.add(formWidget);
			}
			return widgets;
		} catch (ParseException e) {
			return new ExceptionWidget(e);
		}
	}
}
