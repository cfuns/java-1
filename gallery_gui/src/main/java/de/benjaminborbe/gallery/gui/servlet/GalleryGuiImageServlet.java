package de.benjaminborbe.gallery.gui.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.gallery.api.GalleryImage;
import de.benjaminborbe.gallery.api.GalleryImageIdentifier;
import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.gallery.api.GalleryServiceException;
import de.benjaminborbe.gallery.gui.GalleryGuiConstants;
import de.benjaminborbe.tools.util.StreamUtil;

@Singleton
public class GalleryGuiImageServlet extends HttpServlet {

	private static final long serialVersionUID = -5013723680643328782L;

	private final GalleryService galleryService;

	private final StreamUtil streamUtil;

	private final Logger logger;

	@Inject
	public GalleryGuiImageServlet(final Logger logger, final GalleryService galleryService, final StreamUtil streamUtil) {
		this.logger = logger;
		this.galleryService = galleryService;
		this.streamUtil = streamUtil;
	}

	@Override
	protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		try {
			final String imageId = request.getParameter(GalleryGuiConstants.PARAMETER_IMAGE_ID);
			final GalleryImageIdentifier id = galleryService.createGalleryImageIdentifier(imageId);
			final GalleryImage image = galleryService.getImage(id);
			response.setContentType(image.getContentType());
			final ByteArrayInputStream inputStream = new ByteArrayInputStream(image.getContent());
			final OutputStream outputStream = response.getOutputStream();
			streamUtil.copy(inputStream, outputStream);
		}
		catch (final GalleryServiceException e) {
			response.setContentType("text/plain");
			final PrintWriter out = response.getWriter();
			out.println("fail");
			logger.debug(e.getClass().getSimpleName(), e);
		}
	}
}
