package de.benjaminborbe.storage.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.api.StorageValue;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class StorageDeleteServlet extends StorageHtmlServlet {

	private static final long serialVersionUID = 1048276599809672509L;

	private static final String PARAMETER_COLUMNFAMILY = "cf";

	private static final String PARAMETER_ID = "id";

	private static final String PARAMETER_KEY = "key";

	private static final String TITLE = "Storage - Delete";

	private final Logger logger;

	private final StorageService storageService;

	@Inject
	public StorageDeleteServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final NavigationWidget navigationWidget,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final StorageService storageService,
		final CacheService cacheService
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.storageService = storageService;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
		PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final String columnFamily = request.getParameter(PARAMETER_COLUMNFAMILY);
			final String id = request.getParameter(PARAMETER_ID);
			final String key = request.getParameter(PARAMETER_KEY);

			if (columnFamily == null) {
				widgets.add("parameter " + PARAMETER_COLUMNFAMILY + " missing<br>");
			}
			if (id == null) {
				widgets.add("parameter " + PARAMETER_ID + " missing<br>");
			}
			if (key == null) {
				widgets.add("parameter " + PARAMETER_KEY + " missing<br>");
			}
			if (columnFamily != null && id != null && key != null) {
				storageService.delete(columnFamily, new StorageValue(id, storageService.getEncoding()), new StorageValue(key, storageService.getEncoding()));
				widgets.add("deleted");
			}
			return widgets;
		} catch (final Exception e) {
			return new ExceptionWidget(e);
		}
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}
}
