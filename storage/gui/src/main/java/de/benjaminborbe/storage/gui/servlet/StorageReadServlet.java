package de.benjaminborbe.storage.gui.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

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
import de.benjaminborbe.website.util.PreWidget;
import de.benjaminborbe.website.widget.BrWidget;

@Singleton
public class StorageReadServlet extends StorageHtmlServlet {

	private static final long serialVersionUID = 1048276599809672509L;

	private static final String PARAMETER_COLUMNFAMILY = "cf";

	private static final String PARAMETER_ID = "id";

	private static final String PARAMETER_KEY = "key";

	private static final String TITLE = "Storage - Read";

	private final Logger logger;

	private final StorageService persistentStorageService;

	@Inject
	public StorageReadServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final Provider<HttpContext> httpContextProvider,
			final UrlUtil urlUtil,
			final StorageService persistentStorageService,
			final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.persistentStorageService = persistentStorageService;
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

			if (columnFamily != null && id != null && key != null) {
				widgets.add("value=");
				widgets.add(new BrWidget());
				final StorageValue value = persistentStorageService.get(columnFamily, new StorageValue(id, getEncoding()), new StorageValue(key, getEncoding()));
				widgets.add(new PreWidget(StringEscapeUtils.escapeHtml(value.getString())));
			}
			else {
				widgets.add("missing parameters.");
				widgets.add(new BrWidget());
				widgets.add("usage: ");
				widgets.add(PARAMETER_COLUMNFAMILY + "=[Column_family]&" + PARAMETER_ID + "=[ID]&" + PARAMETER_KEY + "=[colname]");
				widgets.add(new BrWidget());
				widgets.add("example to read api-data: " + PARAMETER_COLUMNFAMILY + "=api_data&" + PARAMETER_ID + "=[content_key from database]&" + PARAMETER_KEY + "=content");
				widgets.add(new BrWidget());
			}

			return widgets;
		}
		catch (final Exception e) {
			return new ExceptionWidget(e);
		}
	}

	private String getEncoding() {
		return persistentStorageService.getEncoding();
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}
}
