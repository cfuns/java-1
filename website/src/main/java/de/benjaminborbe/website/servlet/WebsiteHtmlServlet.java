package de.benjaminborbe.website.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
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
import de.benjaminborbe.website.widget.HeadWidget;
import de.benjaminborbe.website.widget.HtmlWidget;

@Singleton
public abstract class WebsiteHtmlServlet extends WebsiteWidgetServlet {

	private static final long serialVersionUID = 1544940069187374367L;

	private final Logger logger;

	private final NavigationWidget navigationWidget;

	private final AuthenticationService authenticationService;

	private final ParseUtil parseUtil;

	private final CalendarUtil calendarUtil;

	private final TimeZoneUtil timeZoneUtil;

	private final AuthorizationService authorizationService;

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
			final UrlUtil urlUtil) {
		super(logger, urlUtil, calendarUtil, timeZoneUtil, httpContextProvider, authenticationService);
		this.logger = logger;
		this.calendarUtil = calendarUtil;
		this.timeZoneUtil = timeZoneUtil;
		this.navigationWidget = navigationWidget;
		this.authenticationService = authenticationService;
		this.parseUtil = parseUtil;
		this.authorizationService = authorizationService;
	}

	protected abstract String getTitle();

	protected Collection<Widget> getWidgets() {
		final Set<Widget> widgets = new HashSet<Widget>();
		try {
			widgets.add(navigationWidget);
		}
		catch (final Exception e) {
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
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			if (isLoginRequired()) {
				if (!authenticationService.isLoggedIn(sessionIdentifier)) {
					return new RedirectWidget(buildLoginUrl(request));
				}
				if (isAdminRequired()) {
					authorizationService.expectAdminRole(sessionIdentifier);
				}
			}
		}
		catch (final AuthenticationServiceException e) {
			final Widget widget = new HtmlWidget(new ExceptionWidget(e));
			return widget;
		}
		catch (final AuthorizationServiceException e) {
			final Widget widget = new HtmlWidget(new ExceptionWidget(e));
			return widget;
		}
		catch (final PermissionDeniedException e) {
			final Widget widget = new HtmlWidget(new ExceptionWidget(e));
			return widget;
		}
		try {
			return createHtmlWidget(request, response, context);
		}
		catch (final PermissionDeniedException e) {
			final Widget widget = new HtmlWidget(new ExceptionWidget(e));
			return widget;
		}
		catch (final RedirectException e) {
			return new RedirectWidget(e.getTarget());
		}
		catch (final LoginRequiredException e) {
			return new RedirectWidget(buildLoginUrl(request));
		}
	}

	private Widget createHtmlWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException, RedirectException,
			PermissionDeniedException, LoginRequiredException {
		final ListWidget widgets = new ListWidget();
		logger.trace("printHtml");
		widgets.add(createHeadWidget(request, response, context));
		widgets.add(createBodyWidget(request, response, context));
		return new TagWidget("html", widgets);
	}

	private Widget createBodyWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException, PermissionDeniedException,
			RedirectException, LoginRequiredException {
		logger.trace("printBody");
		final ListWidget widgets = new ListWidget();
		widgets.add(new DivWidget(createTopWidget(request, response, context)).addAttribute("id", "header"));
		widgets.add(new DivWidget(createContentWidget(request, response, context)).addAttribute("id", "content"));
		widgets.add(new DivWidget(createFooterWidget(request, response, context)).addAttribute("id", "footer"));
		return new TagWidget("body", widgets);
	}

	private Widget createTopWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException, PermissionDeniedException {
		logger.trace("printTop");
		final ListWidget widgets = new ListWidget();
		widgets.add(navigationWidget);
		widgets.add(createLoginStatusWidget(request, response, context));
		return widgets;
	}

	private Widget createLoginStatusWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException {
		try {
			final ListWidget widgets = new ListWidget();
			final SessionIdentifier sessionId = authenticationService.createSessionIdentifier(request);
			if (authenticationService.isLoggedIn(sessionId)) {
				widgets.add("logged in as " + authenticationService.getCurrentUser(sessionId) + " ");
				widgets.add(new LinkRelativWidget(request, "/authentication/logout", "logout"));
			}
			else {
				widgets.add(new LinkRelativWidget(request, "/authentication/login", "login"));
			}
			return new SpanWidget(widgets).addAttribute("id", "loginStatus");
		}
		catch (final AuthenticationServiceException e) {
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

	private Widget createFooterWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		logger.trace("printFooter");
		final ListWidget widgets = new ListWidget();
		widgets.add(new RequestDurationWidget(logger, parseUtil, calendarUtil, timeZoneUtil));
		return widgets;
	}

	private Widget createHeadWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException, PermissionDeniedException {
		logger.trace("printHead");
		return new HeadWidget(getTitle(), getJavascriptResources(request, response), getCssResources(request, response));
	}

	protected List<JavascriptResource> getJavascriptResources(final HttpServletRequest request, final HttpServletResponse response) {
		logger.trace("getJavascriptResources");
		final List<JavascriptResource> result = new ArrayList<JavascriptResource>();
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
		final Set<CssResource> result = new HashSet<CssResource>();
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

	/**
	 * default login is required for each html-servlet
	 */
	@Override
	protected boolean isLoginRequired() {
		return true;
	}

	/**
	 * default admin-role is required for each html-servlet
	 */
	protected boolean isAdminRequired() {
		return true;
	}
}
