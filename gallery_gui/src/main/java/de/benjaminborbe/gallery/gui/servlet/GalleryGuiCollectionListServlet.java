package de.benjaminborbe.gallery.gui.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.gallery.api.GalleryCollection;
import de.benjaminborbe.gallery.api.GalleryGroup;
import de.benjaminborbe.gallery.api.GalleryGroupIdentifier;
import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.gallery.api.GalleryServiceException;
import de.benjaminborbe.gallery.gui.GalleryGuiConstants;
import de.benjaminborbe.gallery.gui.util.GalleryCollectionComparator;
import de.benjaminborbe.gallery.gui.util.GalleryGuiLinkFactory;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;

@Singleton
public class GalleryGuiCollectionListServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Gallery - Collections";

	private final GalleryService galleryService;

	private final Logger logger;

	private final GalleryGuiLinkFactory linkFactory;

	private final AuthenticationService authenticationService;

	private final GalleryCollectionComparator galleryCollectionComparator;

	@Inject
	public GalleryGuiCollectionListServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final AuthenticationService authenticationService,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider,
			final RedirectUtil redirectUtil,
			final UrlUtil urlUtil,
			final GalleryGuiLinkFactory linkFactory,
			final GalleryService galleryService,
			final AuthorizationService authorizationService,
			final GalleryCollectionComparator galleryCollectionComparator) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.linkFactory = linkFactory;
		this.galleryService = galleryService;
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.galleryCollectionComparator = galleryCollectionComparator;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final ListWidget widgets = new ListWidget();

			final GalleryGroupIdentifier galleryGroupIdentifier = galleryService.createGroupIdentifier(request.getParameter(GalleryGuiConstants.PARAMETER_GROUP_ID));
			final GalleryGroup galleryGroup = galleryService.getGroup(sessionIdentifier, galleryGroupIdentifier);

			widgets.add(new H1Widget(galleryGroup.getName() + " - Collections"));
			final UlWidget ul = new UlWidget();

			final ArrayList<GalleryCollection> collections = Lists.newArrayList(galleryService.getCollectionsWithGroup(sessionIdentifier, galleryGroupIdentifier));
			Collections.sort(collections, galleryCollectionComparator);

			for (final GalleryCollection galleryCollection : collections) {
				final ListWidget list = new ListWidget();
				list.add(linkFactory.listEntries(request, galleryCollection));
				list.add(" ");
				list.add(linkFactory.deleteCollection(request, galleryCollection.getId()));
				ul.add(list);
			}
			widgets.add(ul);
			widgets.add(linkFactory.createCollection(request, galleryGroupIdentifier));
			return widgets;
		}
		catch (final GalleryServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return new ExceptionWidget(e);
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return new ExceptionWidget(e);
		}
	}
}
