package de.benjaminborbe.proxy.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.proxy.gui.util.ProxyGuiLinkFactory;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.BrWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class ProxyGuiAdminServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Proxy - Admin";

	private final ProxyGuiLinkFactory proxyGuiLinkFactory;

	@Inject
	public ProxyGuiAdminServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final AuthenticationService authenticationService,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final AuthorizationService authorizationService,
		final CacheService cacheService, final ProxyGuiLinkFactory proxyGuiLinkFactory
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.proxyGuiLinkFactory = proxyGuiLinkFactory;
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
		final ListWidget widgets = new ListWidget();
		widgets.add(new H1Widget(getTitle()));
		widgets.add(new H2Widget("Debug"));
		widgets.add(proxyGuiLinkFactory.conversationList(request));
		widgets.add(new H2Widget("Manage Proxy"));
		widgets.add(proxyGuiLinkFactory.startProxy(request));
		widgets.add(new BrWidget());
		widgets.add(proxyGuiLinkFactory.stopProxy(request));
		widgets.add(new BrWidget());
		return widgets;
	}
}
