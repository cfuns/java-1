package de.benjaminborbe.microblog.gui.servlet;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.microblog.api.MicroblogService;
import de.benjaminborbe.microblog.api.MicroblogServiceException;
import de.benjaminborbe.microblog.gui.util.MicroblogGuiLinkFactory;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.BrWidget;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class MicroblogGuiServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Microblog";

	private final MicroblogService microblogService;

	private final Logger logger;

	private final MicroblogGuiLinkFactory microblogGuiLinkFactory;

	@Inject
	public MicroblogGuiServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final AuthenticationService authenticationService,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final MicroblogService microblogService,
		final UrlUtil urlUtil,
		final AuthorizationService authorizationService,
		final MicroblogGuiLinkFactory microblogGuiLinkFactory,
		final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.microblogService = microblogService;
		this.logger = logger;
		this.microblogGuiLinkFactory = microblogGuiLinkFactory;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));
			widgets.add("latest revision: ");
			final MicroblogPostIdentifier rev = microblogService.getLatestPostIdentifier();
			widgets.add(String.valueOf(rev));
			widgets.add(new BrWidget());
			widgets.add(microblogGuiLinkFactory.sendPost(request, rev));
			widgets.add(new BrWidget());
			widgets.add(microblogGuiLinkFactory.sendConversation(request, rev));
			widgets.add(new BrWidget());
			widgets.add(microblogGuiLinkFactory.refreshPost(request));
			widgets.add(new BrWidget());
			widgets.add(microblogGuiLinkFactory.updatePost(request));
			widgets.add(new BrWidget());
			widgets.add(microblogGuiLinkFactory.notificationList(request));
			widgets.add(new BrWidget());
			return widgets;
		} catch (final MicroblogServiceException e) {
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

}
