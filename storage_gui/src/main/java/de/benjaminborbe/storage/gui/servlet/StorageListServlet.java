package de.benjaminborbe.storage.gui.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import de.benjaminborbe.storage.api.StorageIterator;
import de.benjaminborbe.storage.api.StorageService;
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
public class StorageListServlet extends StorageHtmlServlet {

	private static final long serialVersionUID = 1048276599809672509L;

	private static final String PARAMETER_COLUMNFAMILY = "cf";

	private static final String PARAMETER_PREFIX = "prefix";

	private static final String TITLE = "Storage - List";

	private final Logger logger;

	private final StorageService persistentStorageService;

	@Inject
	public StorageListServlet(
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
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException, LoginRequiredException, SuperAdminRequiredException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final String columnFamily = request.getParameter(PARAMETER_COLUMNFAMILY);
			final String prefix = request.getParameter(PARAMETER_PREFIX);
			if (columnFamily == null) {
				widgets.add("parameter " + PARAMETER_COLUMNFAMILY + " missing<br>");
			}
			if (columnFamily != null) {
				widgets.add("value=");
				widgets.add(new BrWidget());
				final StorageIterator i;
				if (prefix != null) {
					i = persistentStorageService.keyIteratorWithPrefix(columnFamily, prefix);
				}
				else {
					i = persistentStorageService.keyIterator(columnFamily);
				}
				final List<String> keys = new ArrayList<String>();
				while (i.hasNext()) {
					keys.add(i.nextString());
				}
				Collections.sort(keys);
				widgets.add(new PreWidget(StringUtils.join(keys, "\n")));
			}
			return widgets;
		}
		catch (final Exception e) {
			return new ExceptionWidget(e);
		}
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}
}
