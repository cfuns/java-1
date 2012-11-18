package de.benjaminborbe.gallery.gui.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.gallery.api.GalleryImage;
import de.benjaminborbe.gallery.api.GalleryImageIdentifier;
import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.gallery.gui.GalleryGuiConstants;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.StreamUtil;
import de.benjaminborbe.website.servlet.WebsiteServlet;

@Singleton
public class GalleryGuiImageServlet extends WebsiteServlet {

	private static final long serialVersionUID = -5013723680643328782L;

	private final GalleryService galleryService;

	private final StreamUtil streamUtil;

	private final Logger logger;

	private final AuthenticationService authenticationService;

	private final CalendarUtil calendarUtil;

	@Inject
	public GalleryGuiImageServlet(
			final Logger logger,
			final GalleryService galleryService,
			final StreamUtil streamUtil,
			final UrlUtil urlUtil,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final AuthenticationService authenticationService,
			final Provider<HttpContext> httpContextProvider) {
		super(logger, urlUtil, authenticationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.logger = logger;
		this.galleryService = galleryService;
		this.streamUtil = streamUtil;
		this.authenticationService = authenticationService;
		this.calendarUtil = calendarUtil;
	}

	@Override
	protected void doService(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException {
		try {
			// load image
			final String imageId = request.getParameter(GalleryGuiConstants.PARAMETER_IMAGE_ID);
			final GalleryImageIdentifier id = galleryService.createImageIdentifier(imageId);
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final GalleryImage image = galleryService.getImage(sessionIdentifier, id);
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
		}
		catch (final Exception e) {
			response.setContentType("text/plain");
			final PrintWriter out = response.getWriter();
			out.println("fail");
			logger.debug(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	protected boolean isLoginRequired() {
		return false;
	}

}
