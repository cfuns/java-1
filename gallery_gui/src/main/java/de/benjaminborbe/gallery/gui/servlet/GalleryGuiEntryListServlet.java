package de.benjaminborbe.gallery.gui.servlet;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

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
import de.benjaminborbe.authentication.api.SuperAdminRequiredException;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.gallery.api.GalleryCollection;
import de.benjaminborbe.gallery.api.GalleryCollectionIdentifier;
import de.benjaminborbe.gallery.api.GalleryEntry;
import de.benjaminborbe.gallery.api.GalleryGroup;
import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.gallery.api.GalleryServiceException;
import de.benjaminborbe.gallery.gui.GalleryGuiConstants;
import de.benjaminborbe.gallery.gui.util.GalleryEntryComparator;
import de.benjaminborbe.gallery.gui.util.GalleryGuiLinkFactory;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;
import de.benjaminborbe.website.widget.BrWidget;
import de.benjaminborbe.website.widget.ImageWidget;

@Singleton
public class GalleryGuiEntryListServlet extends GalleryGuiHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Gallery - Images";

	private final GalleryService galleryService;

	private final UrlUtil urlUtil;

	private final Logger logger;

	private final GalleryGuiLinkFactory galleryGuiLinkFactory;

	private final AuthenticationService authenticationService;

	private final GalleryEntryComparator galleryEntryComparator;

	@Inject
	public GalleryGuiEntryListServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final AuthenticationService authenticationService,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider,
			final RedirectUtil redirectUtil,
			final UrlUtil urlUtil,
			final GalleryService galleryService,
			final AuthorizationService authorizationService,
			final GalleryGuiLinkFactory galleryGuiLinkFactory,
			final GalleryEntryComparator galleryEntryComparator) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.galleryService = galleryService;
		this.logger = logger;
		this.urlUtil = urlUtil;
		this.galleryGuiLinkFactory = galleryGuiLinkFactory;
		this.authenticationService = authenticationService;
		this.galleryEntryComparator = galleryEntryComparator;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createGalleryContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException, LoginRequiredException, SuperAdminRequiredException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final String galleryId = request.getParameter(GalleryGuiConstants.PARAMETER_COLLECTION_ID);
			final GalleryCollectionIdentifier galleryCollectionIdentifier = galleryService.createCollectionIdentifier(galleryId);
			final GalleryCollection galleryCollection = galleryService.getCollection(sessionIdentifier, galleryCollectionIdentifier);
			final GalleryGroup galleryGroup = galleryService.getGroup(sessionIdentifier, galleryCollection.getGroupId());

			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(galleryGroup.getName() + "/" + galleryCollection.getName() + " - Images"));
			final UlWidget ul = new UlWidget();

			final List<GalleryEntry> entries = Lists.newArrayList(galleryService.getEntries(sessionIdentifier, galleryCollectionIdentifier));
			Collections.sort(entries, galleryEntryComparator);

			for (int i = 0; i < entries.size(); ++i) {
				final GalleryEntry galleryEntry = entries.get(i);
				final ListWidget list = new ListWidget();
				list.add(new ImageWidget(galleryGuiLinkFactory.createImage(request, galleryEntry.getPreviewImageIdentifier())));
				list.add(new ImageWidget(galleryGuiLinkFactory.createImage(request, galleryEntry.getImageIdentifier())));
				list.add(new BrWidget());

				if (i > 0) {
					list.add(galleryGuiLinkFactory.swapEntryPrio(request, galleryEntry.getId(), entries.get(i - 1).getId(), "up"));
					list.add(" ");
				}
				if (i < entries.size() - 1) {
					list.add(galleryGuiLinkFactory.swapEntryPrio(request, galleryEntry.getId(), entries.get(i + 1).getId(), "down"));
					list.add(" ");
				}
				list.add(galleryGuiLinkFactory.updateEntry(request, galleryEntry.getId()));
				list.add(" ");
				list.add(galleryGuiLinkFactory.deleteEntry(request, galleryEntry.getId()));
				ul.add(list);
			}
			ul.addAttribute("class", "entrylist");
			widgets.add(ul);
			widgets.add(new LinkRelativWidget(urlUtil, request, "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_ENTRY_CREATE, new MapParameter().add(
					GalleryGuiConstants.PARAMETER_COLLECTION_ID, String.valueOf(galleryCollectionIdentifier)), "upload image"));
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
