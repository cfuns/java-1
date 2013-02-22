package de.benjaminborbe.microblog.gui.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.microblog.api.MicroblogService;
import de.benjaminborbe.microblog.api.MicroblogServiceException;
import de.benjaminborbe.microblog.gui.MicroblogGuiConstants;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;

@Singleton
public class MicroblogGuiConversationSendServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Microblog - Send Conversation";

	private final MicroblogService microblogService;

	private final Logger logger;

	private final ParseUtil parseUtil;

	@Inject
	public MicroblogGuiConversationSendServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final AuthenticationService authenticationService,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider,
			final MicroblogService microblogService,
			final RedirectUtil redirectUtil,
			final UrlUtil urlUtil,
			final AuthorizationService authorizationService,
			final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.microblogService = microblogService;
		this.logger = logger;
		this.parseUtil = parseUtil;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		logger.trace("printContent");
		final ListWidget widgets = new ListWidget();
		widgets.add(new H1Widget(getTitle()));
		try {
			final long rev = parseUtil.parseLong(request.getParameter(MicroblogGuiConstants.PARAMETER_POST_ID));
			microblogService.mailConversation(microblogService.createMicroblogConversationIdentifier(rev));
			widgets.add("send post with revision " + rev + " done");
		}
		catch (final ParseException e) {
			widgets.add("parameter " + MicroblogGuiConstants.PARAMETER_POST_ID + " missing");
		}
		catch (final MicroblogServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
		return widgets;
	}
}
