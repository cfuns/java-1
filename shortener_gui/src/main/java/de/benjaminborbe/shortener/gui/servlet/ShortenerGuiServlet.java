package de.benjaminborbe.shortener.gui.servlet;

import java.io.IOException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.shortener.api.ShortenerService;
import de.benjaminborbe.shortener.api.ShortenerServiceException;
import de.benjaminborbe.shortener.api.ShortenerUrlIdentifier;
import de.benjaminborbe.shortener.gui.ShortenerGuiConstants;
import de.benjaminborbe.shortener.gui.util.ShortenerGuiLinkFactory;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.ValidationExceptionWidget;

@Singleton
public class ShortenerGuiServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Shortener";

	private final ParseUtil parseUtil;

	private final ShortenerService shortenerService;

	private final Logger logger;

	private final ShortenerGuiLinkFactory shortenerGuiLinkFactory;

	@Inject
	public ShortenerGuiServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final AuthenticationService authenticationService,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider,
			final RedirectUtil redirectUtil,
			final UrlUtil urlUtil,
			final AuthorizationService authorizationService,
			final ShortenerService shortenerService,
			final ShortenerGuiLinkFactory shortenerGuiLinkFactory) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.parseUtil = parseUtil;
		this.shortenerService = shortenerService;
		this.logger = logger;
		this.shortenerGuiLinkFactory = shortenerGuiLinkFactory;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final String urlString = request.getParameter(ShortenerGuiConstants.PARAMETER_URL);
			if (urlString != null) {
				try {
					final URL url = parseUtil.parseURL(urlString);
					final ShortenerUrlIdentifier shortenerUrlIdentifier = shortenerService.shorten(url);
					widgets.add(shortenerGuiLinkFactory.getRedirectUrl(request, shortenerUrlIdentifier));
				}
				catch (final ParseException e) {
					widgets.add("invalid url");
				}
				catch (final ValidationException e) {
					widgets.add("shorten url failed!");
					widgets.add(new ValidationExceptionWidget(e));
				}
			}

			final FormWidget formWidget = new FormWidget();
			formWidget.addFormInputWidget(new FormInputTextWidget(ShortenerGuiConstants.PARAMETER_URL).addLabel("Url:").addPlaceholder("http://..."));
			formWidget.addFormInputWidget(new FormInputSubmitWidget("shorten"));
			widgets.add(formWidget);
			return widgets;
		}
		catch (final ShortenerServiceException e) {
			logger.trace(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}
}
