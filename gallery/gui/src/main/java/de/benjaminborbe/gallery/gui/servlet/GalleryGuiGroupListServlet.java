package de.benjaminborbe.gallery.gui.servlet;

import com.google.common.collect.Lists;
import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.gallery.api.GalleryGroup;
import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.gallery.api.GalleryServiceException;
import de.benjaminborbe.gallery.gui.util.GalleryGroupComparator;
import de.benjaminborbe.gallery.gui.util.GalleryGuiLinkFactory;
import de.benjaminborbe.gallery.gui.util.GalleryGuiWebsiteHtmlServlet;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Singleton
public class GalleryGuiGroupListServlet extends GalleryGuiWebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Gallery - Groups";

	private final GalleryService galleryService;

	private final Logger logger;

	private final GalleryGuiLinkFactory linkFactory;

	private final AuthenticationService authenticationService;

	private final GalleryGroupComparator galleryGroupComparator;

	@Inject
	public GalleryGuiGroupListServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final AuthenticationService authenticationService,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final GalleryGuiLinkFactory linkFactory,
		final GalleryService galleryService,
		final AuthorizationService authorizationService,
		final GalleryGroupComparator galleryGroupComparator,
		final CacheService cacheService
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService, galleryService);
		this.linkFactory = linkFactory;
		this.galleryService = galleryService;
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.galleryGroupComparator = galleryGroupComparator;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createGalleryContentWidget(final HttpServletRequest request) throws IOException,
		PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(TITLE));
			final UlWidget ul = new UlWidget();
			final List<GalleryGroup> groups = Lists.newArrayList(galleryService.getGroups(sessionIdentifier));
			Collections.sort(groups, galleryGroupComparator);
			for (final GalleryGroup galleryGroup : groups) {
				final ListWidget list = new ListWidget();
				list.add(linkFactory.listCollections(request, galleryGroup));
				list.add(" ");
				list.add(linkFactory.updateGroup(request, galleryGroup.getId()));
				list.add(" ");
				list.add(linkFactory.deleteGroup(request, galleryGroup.getId()));
				ul.add(list);
			}
			widgets.add(ul);
			widgets.add(linkFactory.createGroup(request));
			return widgets;
		} catch (final GalleryServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return new ExceptionWidget(e);
		} catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return new ExceptionWidget(e);
		}
	}
}
