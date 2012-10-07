package de.benjaminborbe.gallery.gui.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.gallery.api.GalleryIdentifier;
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
import de.benjaminborbe.website.form.FormEncType;
import de.benjaminborbe.website.form.FormInputFileWidget;
import de.benjaminborbe.website.form.FormInputHiddenWidget;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormMethod;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.link.LinkRelativWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.BrWidget;

@Singleton
public class GalleryGuiImageUploadServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Gallery - Upload";

	private final GalleryService galleryService;

	private final Logger logger;

	private final UrlUtil urlUtil;

	@Inject
	public GalleryGuiImageUploadServlet(
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
		this.logger = logger;
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
			widgets.add(new H1Widget(getTitle()));
			final GalleryIdentifier galleryIdentifier = galleryService.createGalleryIdentifier(request.getParameter(GalleryGuiConstants.PARAMETER_GALLERY_ID));

			final boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if (isMultipart) {
				// Create a factory for disk-based file items
				final FileItemFactory factory = new DiskFileItemFactory();

				// Create a new file upload handler
				final ServletFileUpload upload = new ServletFileUpload(factory);

				// Parse the request
				try {
					@SuppressWarnings("unchecked")
					final List<FileItem> items = upload.parseRequest(request);
					for (final FileItem item : items) {
						if (!item.isFormField()) {
							final String imageName = item.getFieldName();
							final byte[] imageContent = item.get();
							final String imageContentType = extractContentType(item.getContentType(), imageName);
							galleryService.saveImage(galleryIdentifier, imageName, imageContentType, imageContent);
							widgets.add("file " + item.getName() + " uploaded!");
							widgets.add(new BrWidget());
							widgets.add(new LinkRelativWidget(urlUtil, request, "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_IMAGE_LIST, new MapChain<String, String>().add(
									GalleryGuiConstants.PARAMETER_GALLERY_ID, String.valueOf(galleryIdentifier)), "list"));
						}
					}
				}
				catch (final FileUploadException e) {
					logger.warn(e.getClass().getSimpleName(), e);
					widgets.add(new ExceptionWidget(e));
				}
			}

			final FormWidget form = new FormWidget().addEncType(FormEncType.MULTIPART).addMethod(FormMethod.POST);
			form.addFormInputWidget(new FormInputHiddenWidget(GalleryGuiConstants.PARAMETER_GALLERY_ID));
			form.addFormInputWidget(new FormInputFileWidget(GalleryGuiConstants.PARAMETER_UPLOAD).addLabel("Image"));
			form.addFormInputWidget(new FormInputSubmitWidget("upload"));
			widgets.add(form);

			return widgets;
		}
		catch (final GalleryServiceException e) {
			logger.warn(e.getClass().getSimpleName(), e);
			return new ExceptionWidget(e);
		}
	}

	private String extractContentType(final String contentType, final String imageName) {
		if (contentType != null) {
			return contentType;
		}
		else if (imageName.indexOf(".jpg") != -1 || imageName.indexOf(".jpeg") != -1) {
			return "image/jpeg";
		}
		else if (imageName.indexOf(".png") != -1) {
			return "image/png";
		}
		else {
			return null;
		}
	}
}
