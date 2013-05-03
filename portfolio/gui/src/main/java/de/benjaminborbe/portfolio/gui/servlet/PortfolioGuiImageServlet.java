package de.benjaminborbe.portfolio.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.gallery.api.GalleryImage;
import de.benjaminborbe.gallery.api.GalleryImageIdentifier;
import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.portfolio.gui.PortfolioGuiConstants;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.servlet.WebsiteServlet;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Singleton
public class PortfolioGuiImageServlet extends WebsiteServlet {

	private static final long serialVersionUID = -5013723680643328782L;

	private final GalleryService galleryService;

	private final Logger logger;

	private final UrlUtil urlUtil;

	@Inject
	public PortfolioGuiImageServlet(
		final Logger logger,
		final GalleryService galleryService,
		final UrlUtil urlUtil,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final AuthenticationService authenticationService,
		final Provider<HttpContext> httpContextProvider,
		final AuthorizationService authorizationService
	) {
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.logger = logger;
		this.galleryService = galleryService;
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
			final String imageId = urlUtil.parseId(request, PortfolioGuiConstants.PARAMETER_IMAGE_ID);
			final GalleryImageIdentifier id = galleryService.createImageIdentifier(imageId);
			final GalleryImage image = galleryService.getImage(id);
			logger.debug("loaded image " + image);

			// set header
			response.setContentType(image.getContentType());

			response.setDateHeader("Last-Modified", image.getModified().getTimeInMillis());
			response.setHeader("ETag", id.getId());

			// output image content
			final byte[] content = image.getContent();
			response.setContentLength(content.length);
			final ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(content);
		} catch (final Exception e) {
			response.setContentType("text/plain");
			final PrintWriter out = response.getWriter();
			out.println("fail");
			logger.warn(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public boolean isLoginRequired() {
		return false;
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}

}
