package de.benjaminborbe.gallery.gui.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.gallery.api.GalleryCollectionIdentifier;
import de.benjaminborbe.gallery.api.GalleryEntryIdentifier;
import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.gallery.api.GalleryServiceException;
import de.benjaminborbe.gallery.gui.GalleryGuiConstants;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.form.FormEncType;
import de.benjaminborbe.website.form.FormInputFileWidget;
import de.benjaminborbe.website.form.FormInputHiddenWidget;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormMethod;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;

@Singleton
public class GalleryGuiEntryCreateServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Gallery - Upload";

	private final GalleryService galleryService;

	private final Logger logger;

	private final AuthenticationService authenticationService;

	@Inject
	public GalleryGuiEntryCreateServlet(
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
			final AuthorizationService authorizationService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.galleryService = galleryService;
		this.logger = logger;
		this.authenticationService = authenticationService;
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

			final Map<String, FileItem> files = new HashMap<String, FileItem>();
			final Map<String, String> parameter = new HashMap<String, String>();
			final boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if (isMultipart) {
				logger.info("isMultipart");
				// Create a factory for disk-based file items
				final FileItemFactory factory = new DiskFileItemFactory();

				// Create a new file upload handler
				final ServletFileUpload upload = new ServletFileUpload(factory);

				// Parse the request
				try {
					@SuppressWarnings("unchecked")
					final List<FileItem> items = upload.parseRequest(request);
					for (final FileItem item : items) {
						final String itemName = item.getFieldName();
						if (!item.isFormField()) {
							logger.info("found image: '" + itemName + "'");
							files.put(itemName, item);
						}
						else {
							logger.info("parameter " + itemName + " = " + item.getString());
							parameter.put(itemName, item.getString());
						}
					}
				}
				catch (final FileUploadException e) {
					logger.debug(e.getClass().getName(), e);
					widgets.add(new ExceptionWidget(e));
				}

				if (parameter.containsKey(GalleryGuiConstants.PARAMETER_COLLECTION_ID) && parameter.containsKey(GalleryGuiConstants.PARAMETER_COLLECTION_ID)
						&& files.containsKey(GalleryGuiConstants.PARAMETER_IMAGE_CONTENT) && files.containsKey(GalleryGuiConstants.PARAMETER_IMAGE_CONTENT_PREVIEW)
						&& files.containsKey(GalleryGuiConstants.PARAMETER_IMAGE_CONTENT_PREVIEW)) {
					final FileItem imagePreviewItem = files.get(GalleryGuiConstants.PARAMETER_IMAGE_CONTENT_PREVIEW);
					final String imagePreviewName = imagePreviewItem.getFieldName();
					final byte[] imagePreviewContent = imagePreviewItem.get();
					final String imagePreviewContentType = extractContentType(imagePreviewItem.getContentType(), imagePreviewName);

					final FileItem imageItem = files.get(GalleryGuiConstants.PARAMETER_IMAGE_CONTENT);
					final String imageName = imageItem.getFieldName();
					final byte[] imageContent = imageItem.get();
					final String imageContentType = extractContentType(imageItem.getContentType(), imageName);

					final String galleryId = parameter.get(GalleryGuiConstants.PARAMETER_COLLECTION_ID);
					final String name = parameter.get(GalleryGuiConstants.PARAMETER_IMAGE_NAME);

					final GalleryCollectionIdentifier galleryIdentifier = galleryService.createCollectionIdentifier(galleryId);
					final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
					final GalleryEntryIdentifier entryIdentifier = galleryService.createEntry(sessionIdentifier, galleryIdentifier, name, imagePreviewName, imagePreviewContent,
							imagePreviewContentType, imageName, imageContent, imageContentType);
					widgets.add("images uploaded!");
					logger.debug("entryIdentifier: " + entryIdentifier);
				}
				else {
					logger.info("parameter missing");
				}
			}

			final FormWidget form = new FormWidget().addEncType(FormEncType.MULTIPART).addMethod(FormMethod.POST);
			form.addFormInputWidget(new FormInputHiddenWidget(GalleryGuiConstants.PARAMETER_COLLECTION_ID));
			form.addFormInputWidget(new FormInputTextWidget(GalleryGuiConstants.PARAMETER_IMAGE_NAME).addLabel("Name").addPlaceholder("name..."));
			form.addFormInputWidget(new FormInputFileWidget(GalleryGuiConstants.PARAMETER_IMAGE_CONTENT_PREVIEW).addLabel("Preview"));
			form.addFormInputWidget(new FormInputFileWidget(GalleryGuiConstants.PARAMETER_IMAGE_CONTENT).addLabel("Image"));
			form.addFormInputWidget(new FormInputSubmitWidget("upload"));
			widgets.add(form);

			return widgets;
		}
		catch (final GalleryServiceException e) {
			logger.warn(e.getClass().getSimpleName(), e);
			return new ExceptionWidget(e);
		}
		catch (final AuthenticationServiceException e) {
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
