package de.benjaminborbe.task.gui.servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.google.inject.Provider;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.task.gui.TaskGuiConstants;
import de.benjaminborbe.task.gui.util.TaskGuiUtil;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.CssResourceImpl;
import de.benjaminborbe.website.util.DivWidget;
import de.benjaminborbe.website.util.JavascriptResourceImpl;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.SpanWidget;

public abstract class TaskGuiWebsiteHtmlServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 3963074728162589267L;

	private final TaskGuiUtil taskGuiUtil;

	public TaskGuiWebsiteHtmlServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final Provider<HttpContext> httpContextProvider,
			final UrlUtil urlUtil,
			final TaskGuiUtil taskGuiUtil) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.taskGuiUtil = taskGuiUtil;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException, LoginRequiredException {

		final ListWidget widgets = new ListWidget();
		widgets.add(createTaskContentWidget(request, response, context));

		final SpanWidget tooltip = new SpanWidget();
		tooltip.addAttribute("id", "tooltip");
		tooltip.addAttribute("style", "display:none;");
		widgets.add(tooltip);

		final List<String> selectedTaskcontextIds = taskGuiUtil.getSelectedTaskContextIds(request);
		if (selectedTaskcontextIds != null) {
			final Cookie cookie = new Cookie(TaskGuiConstants.COOKIE_TASKCONTEXTS, StringUtils.join(selectedTaskcontextIds, ","));
			cookie.setPath(request.getContextPath() + "/");
			cookie.setSecure(false);
			// 7 days
			cookie.setMaxAge(7 * 24 * 60 * 60);
			response.addCookie(cookie);
		}

		return new DivWidget(widgets).addAttribute("class", "task");
	}

	protected abstract Widget createTaskContentWidget(HttpServletRequest request, HttpServletResponse response, HttpContext context) throws IOException, PermissionDeniedException,
			RedirectException, LoginRequiredException;

	@Override
	protected Collection<CssResource> getCssResources(final HttpServletRequest request, final HttpServletResponse response) {
		final Collection<CssResource> result = super.getCssResources(request, response);
		result.add(new CssResourceImpl(request.getContextPath() + "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_CSS_STYLE));
		return result;
	}

	@Override
	protected List<JavascriptResource> getJavascriptResources(final HttpServletRequest request, final HttpServletResponse response) {
		final List<JavascriptResource> result = super.getJavascriptResources(request, response);
		result.add(new JavascriptResourceImpl(request.getContextPath() + "/" + TaskGuiConstants.URL_JS_SCRIPT));
		return result;
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}

}
