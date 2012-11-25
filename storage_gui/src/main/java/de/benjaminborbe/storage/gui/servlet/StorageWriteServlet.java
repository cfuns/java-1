package de.benjaminborbe.storage.gui.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SuperAdminRequiredException;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.BrWidget;

@Singleton
public class StorageWriteServlet extends StorageHtmlServlet {

	private static final long serialVersionUID = 1048276599809672509L;

	private static final String PARAMETER_COLUMNFAMILY = "cf";

	private static final String PARAMETER_ID = "id";

	private static final String PARAMETER_KEY = "key";

	private static final String PARAMETER_VALUE = "value";

	private static final String TITLE = "Storage - Write";

	private final Logger logger;

	private final StorageService persistentStorageService;

	@Inject
	public StorageWriteServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final Provider<HttpContext> httpContextProvider,
			final UrlUtil urlUtil,
			final StorageService persistentStorageService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.logger = logger;
		this.persistentStorageService = persistentStorageService;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException, LoginRequiredException, SuperAdminRequiredException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final String columnFamily = request.getParameter(PARAMETER_COLUMNFAMILY);
			final String id = request.getParameter(PARAMETER_ID);
			final String key = request.getParameter(PARAMETER_KEY);
			final String value = request.getParameter(PARAMETER_VALUE);

			if (columnFamily == null) {
				widgets.add("parameter " + PARAMETER_COLUMNFAMILY + " missing");
				widgets.add(new BrWidget());
			}
			if (id == null) {
				widgets.add("parameter " + PARAMETER_ID + " missing");
				widgets.add(new BrWidget());
			}
			if (key == null) {
				widgets.add("parameter " + PARAMETER_KEY + " missing");
				widgets.add(new BrWidget());
			}
			if (value == null) {
				widgets.add("parameter " + PARAMETER_VALUE + " missing");
				widgets.add(new BrWidget());
			}
			if (columnFamily != null && id != null && key != null && value != null) {
				persistentStorageService.set(columnFamily, id, key, value);
				widgets.add("write");
			}

			return widgets;
		}
		catch (final Exception e) {
			return new ExceptionWidget(e);
		}
	}

}
