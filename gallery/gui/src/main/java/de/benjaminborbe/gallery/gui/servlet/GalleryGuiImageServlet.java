package de.benjaminborbe.gallery.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.gallery.api.GalleryImage;
import de.benjaminborbe.gallery.api.GalleryImageIdentifier;
import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.gallery.gui.GalleryGuiConstants;
import de.benjaminborbe.gallery.gui.util.GalleryGuiWebsiteServlet;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.stream.StreamUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

@Singleton
public class GalleryGuiImageServlet extends GalleryGuiWebsiteServlet {

	private static final long serialVersionUID = -5013723680643328782L;

	private final GalleryService galleryService;

	private final StreamUtil streamUtil;

	private final Logger logger;

	private final CalendarUtil calendarUtil;

	private final UrlUtil urlUtil;

	@Inject
	public GalleryGuiImageServlet(
		final Logger logger,
		final GalleryService galleryService,
		final StreamUtil streamUtil,
		final UrlUtil urlUtil,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final AuthenticationService authenticationService,
		final Provider<HttpContext> httpContextProvider,
		final AuthorizationService authorizationService
	) {
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider, galleryService);
		this.logger = logger;
		this.galleryService = galleryService;
		this.streamUtil = streamUtil;
		this.calendarUtil = calendarUtil;
		this.urlUtil = urlUtil;
	}

	@Override
	protected void doService(
		final HttpServletRequest request,
		final HttpServletResponse response,
		final HttpContext context
	) throws ServletException, IOException {
		try {
			// load image
			final String imageId = urlUtil.parseId(request, GalleryGuiConstants.PARAMETER_IMAGE_ID);
			final GalleryImageIdentifier id = galleryService.createImageIdentifier(imageId);
			final GalleryImage image = galleryService.getImage(id);
			logger.info("loaded image " + image);

			// set header
			response.setContentType(image.getContentType());
			// seconds
			final int cacheAge = 24 * 60 * 60;
			final long expiry = calendarUtil.getTime() + cacheAge * 1000;
			response.setDateHeader("Expires", expiry);
			response.setHeader("Cache-Control", "max-age=" + cacheAge);

			response.setDateHeader("Last-Modified", image.getModified().getTimeInMillis());
			response.setHeader("ETag", id.getId());

			// output image content
			final ByteArrayInputStream inputStream = new ByteArrayInputStream(image.getContent());
			final OutputStream outputStream = response.getOutputStream();
			streamUtil.copy(inputStream, outputStream);
		} catch (final Exception e) {
			response.setContentType("text/plain");
			final PrintWriter out = response.getWriter();
			out.println("fail");
			logger.debug(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public boolean isLoginRequired() {
		return false;
	}

}
