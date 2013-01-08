package de.benjaminborbe.crawler.gui.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.crawler.api.CrawlerException;
import de.benjaminborbe.crawler.api.CrawlerInstruction;
import de.benjaminborbe.crawler.api.CrawlerInstructionBuilder;
import de.benjaminborbe.crawler.api.CrawlerService;
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
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;

@Singleton
public class CrawlerGuiServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Crawler";

	private static final String PARAMETER_URL = "url";

	private static final int TIMEOUT = 5000;

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
			final RedirectUtil redirectUtil,
			final UrlUtil urlUtil,
			final AuthorizationService authorizationService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
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
		logger.trace("printContent");
		final ListWidget widgets = new ListWidget();
		widgets.add(new H1Widget(getTitle()));
		if (request.getParameter(PARAMETER_URL) != null) {
			try {
				final String url = request.getParameter(PARAMETER_URL);
				final CrawlerInstruction crawlerInstructionBuilder = new CrawlerInstructionBuilder(url, TIMEOUT);
				crawlerService.processCrawlerInstruction(crawlerInstructionBuilder);
				widgets.add("add url successful");
			}
			catch (final CrawlerException e) {
				logger.debug(e.getClass().getName(), e);
				widgets.add("add url failed");
			}
		}
		else {
			final String action = request.getContextPath() + "/crawler";
			final FormWidget formWidget = new FormWidget(action).addMethod(FormMethod.POST);
			formWidget.addFormInputWidget(new FormInputTextWidget(PARAMETER_URL).addPlaceholder("url..."));
			formWidget.addFormInputWidget(new FormInputSubmitWidget("crawle"));
			widgets.add(formWidget);
		}
		return widgets;
	}
}
