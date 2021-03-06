package de.benjaminborbe.dhl.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.dhl.api.Dhl;
import de.benjaminborbe.dhl.api.DhlService;
import de.benjaminborbe.dhl.api.DhlServiceException;
import de.benjaminborbe.dhl.gui.util.DhlGuiLinkFactory;
import de.benjaminborbe.dhl.gui.util.DhlWebsiteHtmlServlet;
import de.benjaminborbe.dhl.gui.widget.DhlGuiCreateDhlIdentifierLink;
import de.benjaminborbe.dhl.gui.widget.DhlGuiDeleteDhlIdentifierLink;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.link.LinkWidget;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.Target;
import de.benjaminborbe.website.util.UlWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;

@Singleton
public class DhlGuiListServlet extends DhlWebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Dhl - List Tracking";

	private final DhlService dhlService;

	private final Logger logger;

	private final AuthenticationService authenticationService;

	private final AuthorizationService authorizationService;

	private final DhlGuiLinkFactory dhlGuiLinkFactory;

	@Inject
	public DhlGuiListServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final AuthenticationService authenticationService,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final DhlService dhlService,
		final AuthorizationService authorizationService,
		final CacheService cacheService, final DhlGuiLinkFactory dhlGuiLinkFactory
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.dhlService = dhlService;
		this.authenticationService = authenticationService;
		this.authorizationService = authorizationService;
		this.dhlGuiLinkFactory = dhlGuiLinkFactory;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
		PermissionDeniedException, LoginRequiredException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));
			final UlWidget ul = new UlWidget();
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final boolean hasAdminRole = authorizationService.hasAdminRole(sessionIdentifier);

			for (final Dhl dhl : dhlService.getEntries(sessionIdentifier)) {
				final ListWidget row = new ListWidget();
				final URL url = dhlService.buildDhlUrl(sessionIdentifier, dhl.getId());
				final String name = String.valueOf(dhl.getId());
				row.add(new LinkWidget(url, name).addTarget(Target.BLANK));
				row.add(" ");
				row.add(dhl.getStatus());
				row.add(" ");
				if (hasAdminRole) {
					row.add(dhlGuiLinkFactory.notifyStatus(request, dhl.getId()));
					row.add(" ");
				}
				row.add(new DhlGuiDeleteDhlIdentifierLink(request, dhl.getId()));
				ul.add(row);
			}
			widgets.add(ul);
			widgets.add(new DhlGuiCreateDhlIdentifierLink(request));
			return widgets;
		} catch (final AuthenticationServiceException e) {
			return new ExceptionWidget(e);
		} catch (AuthorizationServiceException e) {
			return new ExceptionWidget(e);
		} catch (DhlServiceException e) {
			return new ExceptionWidget(e);
		}
	}
}
