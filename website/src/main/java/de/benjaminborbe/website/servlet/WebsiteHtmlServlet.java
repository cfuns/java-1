package de.benjaminborbe.website.servlet;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.RequireCssResource;
import de.benjaminborbe.html.api.RequireJavascriptResource;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.NetUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;
import de.benjaminborbe.website.util.CssResourceImpl;
import de.benjaminborbe.website.util.DivWidget;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.RedirectWidget;
import de.benjaminborbe.website.util.SpanWidget;
import de.benjaminborbe.website.util.TagWidget;
import de.benjaminborbe.website.widget.ClearFloatWidget;
import de.benjaminborbe.website.widget.HeadWidget;
import de.benjaminborbe.website.widget.HtmlWidget;
import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Singleton
public abstract class WebsiteHtmlServlet extends WebsiteWidgetServlet {

	private static final long serialVersionUID = 1544940069187374367L;

	private final Logger logger;

	private final NavigationWidget navigationWidget;

	private final AuthenticationService authenticationService;

	private final ParseUtil parseUtil;

	private final CalendarUtil calendarUtil;

	private final TimeZoneUtil timeZoneUtil;

	private final CacheService cacheService;

	@Inject
	public WebsiteHtmlServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final NavigationWidget navigationWidget,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final CacheService cacheService) {
		super(logger, urlUtil, calendarUtil, timeZoneUtil, httpContextProvider, authenticationService, authorizationService);
		this.logger = logger;
		this.calendarUtil = calendarUtil;
		this.timeZoneUtil = timeZoneUtil;
		this.navigationWidget = navigationWidget;
		this.authenticationService = authenticationService;
		this.parseUtil = parseUtil;
		this.cacheService = cacheService;
	}

	protected abstract String getTitle();

	protected Collection<Widget> getWidgets() {
		final Set<Widget> widgets = new HashSet<>();
		try {
			widgets.add(navigationWidget);
		} catch (final Exception e) {
		}
		return widgets;
	}

	@Override
	protected void doService(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException, ServletException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		super.doService(request, response, context);
	}

	@Override
	public Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		try {
			return createHtmlWidget(request, response, context);
		} catch (final PermissionDeniedException e) {
			logger.debug(e.getClass().getName(), e);
			final Widget widget = new HtmlWidget(new ExceptionWidget(e));
			return widget;
		} catch (final RedirectException e) {
			logger.trace(e.getClass().getName(), e);
			return new RedirectWidget(e.getTarget());
		} catch (final LoginRequiredException e) {
			logger.debug(e.getClass().getName(), e);
			return new RedirectWidget(buildLoginUrl(request));
		}
	}

	private Widget createHtmlWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException, RedirectException,
		PermissionDeniedException, LoginRequiredException {
		final ListWidget widgets = new ListWidget();
		logger.trace("printHtml");
		widgets.add(createHeadWidget(request, response));
		widgets.add(createBodyWidget(request, response, context));
		return new TagWidget("html", widgets);
	}

	private Widget createBodyWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException, PermissionDeniedException,
		RedirectException, LoginRequiredException {
		logger.trace("printBody");
		final ListWidget widgets = new ListWidget();
		widgets.add(new DivWidget(createTopWidget(request)).addAttribute("id", "header"));
		widgets.add(new DivWidget(createContentWidget(request, response, context)).addAttribute("id", "content"));
		widgets.add(new DivWidget(createFooterWidget()).addAttribute("id", "footer"));
		return new TagWidget("body", widgets);
	}

	private Widget createTopWidget(final HttpServletRequest request) throws IOException, PermissionDeniedException {
		logger.trace("printTop");
		final ListWidget widgets = new ListWidget();
		widgets.add(navigationWidget);
		widgets.add(createLoginStatusWidget(request));
		widgets.add(new ClearFloatWidget());
		return widgets;
	}

	private Widget createLoginStatusWidget(final HttpServletRequest request) throws IOException,
		PermissionDeniedException {
		try {
			final ListWidget widgets = new ListWidget();
			final SessionIdentifier sessionId = authenticationService.createSessionIdentifier(request);
			if (authenticationService.isLoggedIn(sessionId)) {
				final ListWidget row = new ListWidget();
				row.add("logged in as ");
				row.add(new LinkRelativWidget(request, "/authentication/profile", String.valueOf(authenticationService.getCurrentUser(sessionId))));
				row.add(" ");
				widgets.add(row);
				widgets.add(new LinkRelativWidget(request, "/authentication/logout", "logout"));
			} else {
				widgets.add(new LinkRelativWidget(request, "/authentication/login", "login"));
			}
			return new SpanWidget(widgets).addAttribute("id", "loginStatus");
		} catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
		PermissionDeniedException, RedirectException, LoginRequiredException {
		logger.trace("printContent");
		final ListWidget widgets = new ListWidget();
		widgets.add(new H1Widget(getTitle()));
		return widgets;
	}

	private Widget createFooterWidget() throws IOException {
		logger.trace("printFooter");
		final ListWidget widgets = new ListWidget();
		widgets.add(new RequestDurationWidget(logger, parseUtil, calendarUtil, timeZoneUtil, new NetUtil(), cacheService));
		return widgets;
	}

	private Widget createHeadWidget(final HttpServletRequest request, final HttpServletResponse response) throws IOException, PermissionDeniedException {
		logger.trace("printHead");
		return new HeadWidget(getTitle(), getJavascriptResources(request, response), getCssResources(request, response));
	}

	protected List<JavascriptResource> getJavascriptResources(final HttpServletRequest request, final HttpServletResponse response) {
		logger.trace("getJavascriptResources");
		final List<JavascriptResource> result = new ArrayList<>();
		final Collection<Widget> widgets = getWidgets();
		logger.trace("found " + widgets.size() + " widgets");
		for (final Widget widget : widgets) {
			logger.trace("try widget " + widget.getClass().getName());
			if (widget instanceof RequireJavascriptResource) {
				logger.trace("widget " + widget.getClass().getName() + " requireJavascriptResource");
				final RequireJavascriptResource requireJavascriptResource = (RequireJavascriptResource) widget;
				result.addAll(requireJavascriptResource.getJavascriptResource(request, response));
			}
		}
		return result;
	}

	protected Collection<CssResource> getCssResources(final HttpServletRequest request, final HttpServletResponse response) {
		logger.trace("getCssResources");
		final Set<CssResource> result = new HashSet<>();
		final Collection<Widget> widgets = getWidgets();
		logger.trace("found " + widgets.size() + " widgets");
		for (final Widget widget : widgets) {
			logger.trace("try widget " + widget.getClass().getName());
			if (widget instanceof RequireCssResource) {
				logger.trace("widget " + widget.getClass().getName() + " requireJavascriptResource");
				final RequireCssResource requireCssResource = (RequireCssResource) widget;
				result.addAll(requireCssResource.getCssResource(request, response));
			}
		}

		// static
		final String contextPath = request.getContextPath();
		result.add(new CssResourceImpl(contextPath + "/css/style.css"));

		return result;
	}

}
