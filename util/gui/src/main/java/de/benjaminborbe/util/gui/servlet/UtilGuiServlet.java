package de.benjaminborbe.util.gui.servlet;

import javax.inject.Inject;
import com.google.inject.Provider;
import javax.inject.Singleton;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.util.gui.util.UtilLinkFactory;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class UtilGuiServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 3897185107545429460L;

	private static final String TITLE = "Util - Index";

	private final Logger logger;

	private final UtilLinkFactory utilLinkFactory;

	@Inject
	public UtilGuiServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final AuthenticationService authenticationService,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final AuthorizationService authorizationService,
		final UtilLinkFactory utilLinkFactory,
		final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.utilLinkFactory = utilLinkFactory;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
		PermissionDeniedException, RedirectException {
		logger.trace("printContent");
		final ListWidget widgets = new ListWidget();
		widgets.add(new H1Widget(getTitle()));
		final UlWidget ul = new UlWidget();
		ul.add(utilLinkFactory.calc(request));
		ul.add(utilLinkFactory.dayDiff(request));
		ul.add(utilLinkFactory.log(request));
		ul.add(utilLinkFactory.passwordGenerator(request));
		ul.add(utilLinkFactory.penMe(request));
		ul.add(utilLinkFactory.penTest(request));
		ul.add(utilLinkFactory.qunitSample(request));
		ul.add(utilLinkFactory.angularJsSample(request));
		ul.add(utilLinkFactory.time(request));
		ul.add(utilLinkFactory.timeConvert(request));
		ul.add(utilLinkFactory.uuidGenerator(request));
		widgets.add(ul);
		return widgets;
	}

}
