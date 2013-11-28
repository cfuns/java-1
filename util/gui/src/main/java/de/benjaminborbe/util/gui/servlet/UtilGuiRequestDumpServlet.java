package de.benjaminborbe.util.gui.servlet;

import com.google.inject.Provider;
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
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

@Singleton
public class UtilGuiRequestDumpServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 3897185107545429460L;

	private static final String TITLE = "Util - Request - Dump";

	@Inject
	public UtilGuiRequestDumpServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final AuthenticationService authenticationService,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final AuthorizationService authorizationService,
		final CacheService cacheService
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
		PermissionDeniedException, RedirectException {

		final ListWidget widgets = new ListWidget();
		widgets.add(new H1Widget(TITLE));

		{
			widgets.add(new H2Widget("Header"));
			final UlWidget ul = new UlWidget();
			ul.add("RequestURI: " + request.getRequestURI());
			ul.add("RequestURL: " + request.getRequestURL());
			ul.add("Method: " + request.getMethod());
			widgets.add(ul);
		}

		{
			widgets.add(new H2Widget("Header"));
			final UlWidget ul = new UlWidget();
			for (final String headerField : getHeaderFields(request)) {
				final ListWidget values = new ListWidget();
				values.add(headerField);
				values.add(" = ");
				final Enumeration<?> headerValues = request.getHeaders(headerField);
				boolean first = true;
				while (headerValues.hasMoreElements()) {
					if (first) {
						first = false;
					} else {
						values.add(", ");
					}
					values.add(toString(headerValues.nextElement()));
				}
				ul.add(values);
			}
			widgets.add(ul);
		}
		{
			widgets.add(new H2Widget("Parameter"));
			final UlWidget ul = new UlWidget();
			for (final String parameter : getParameters(request)) {
				final ListWidget values = new ListWidget();
				values.add(parameter);
				values.add(" = ");
				boolean first = true;
				for (final String parameterValue : request.getParameterValues(parameter)) {
					if (first) {
						first = false;
					} else {
						values.add(", ");
					}
					values.add(parameterValue);
				}
				ul.add(values);
			}
			widgets.add(ul);
		}

		return widgets;
	}

	private List<String> getHeaderFields(final HttpServletRequest request) {
		return getValues(request.getHeaderNames());
	}

	private List<String> getParameters(final HttpServletRequest request) {
		return getValues(request.getParameterNames());
	}

	private List<String> getValues(final Enumeration<?> enumeration) {
		final List<String> values = new ArrayList<String>();
		while (enumeration.hasMoreElements()) {
			values.add(toString(enumeration.nextElement()));
		}
		Collections.sort(values);
		return values;
	}

	private String toString(final Object o) {
		return o != null ? String.valueOf(o) : null;
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}

	@Override
	public boolean isLoginRequired() {
		return false;
	}
}
