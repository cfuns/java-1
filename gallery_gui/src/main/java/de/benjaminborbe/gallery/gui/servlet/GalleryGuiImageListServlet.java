package de.benjaminborbe.gallery.gui.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.gallery.api.GalleryIdentifier;
import de.benjaminborbe.gallery.api.GalleryImageIdentifier;
import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.gallery.api.GalleryServiceException;
import de.benjaminborbe.gallery.gui.GalleryGuiConstants;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.map.MapChain;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.ImageWidget;
import de.benjaminborbe.website.link.LinkRelativWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;

@Singleton
public class GalleryGuiImageListServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Gallery - Images";

	private final GalleryService galleryService;

	private final UrlUtil urlUtil;

	@Inject
	public GalleryGuiImageListServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final AuthenticationService authenticationService,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider,
			final RedirectUtil redirectUtil,
			final UrlUtil urlUtil,
			final GalleryService galleryService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, httpContextProvider, urlUtil);
		this.galleryService = galleryService;
		this.urlUtil = urlUtil;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(TITLE));

			final String galleryId = request.getParameter(GalleryGuiConstants.PARAMETER_GALLERY_ID);
			final GalleryIdentifier galleryIdentifier = galleryService.createGalleryIdentifier(galleryId);

			final UlWidget ul = new UlWidget();
			for (final GalleryImageIdentifier imageId : galleryService.getImages(galleryIdentifier)) {
				final ListWidget list = new ListWidget();
				list.add(new ImageWidget(request.getContextPath() + "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_IMAGE + "?" + GalleryGuiConstants.PARAMETER_IMAGE_ID + "="
						+ imageId));
				final Map<String, String> data = new HashMap<String, String>();
				data.put(GalleryGuiConstants.PARAMETER_IMAGE_ID, String.valueOf(imageId));
				list.add(new LinkRelativWidget(urlUtil, request, "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_IMAGE_DELETE, data, "delete"));
				ul.add(list);
			}
			widgets.add(ul);
			widgets.add(new LinkRelativWidget(urlUtil, request, "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_IMAGE_UPLOAD, new MapChain<String, String>().add(
					GalleryGuiConstants.PARAMETER_GALLERY_ID, String.valueOf(galleryIdentifier)), "upload image"));
			return widgets;
		}
		catch (final GalleryServiceException e) {
			return new ExceptionWidget(e);
		}
	}
}
