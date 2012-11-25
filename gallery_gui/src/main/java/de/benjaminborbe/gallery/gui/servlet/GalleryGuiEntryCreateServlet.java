package de.benjaminborbe.gallery.gui.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.SuperAdminRequiredException;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.gallery.api.GalleryCollectionIdentifier;
import de.benjaminborbe.gallery.api.GalleryEntryIdentifier;
import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.gallery.api.GalleryServiceException;
import de.benjaminborbe.gallery.gui.GalleryGuiConstants;
import de.benjaminborbe.gallery.gui.util.GalleryGuiLinkFactory;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.validation.ValidationResultImpl;
import de.benjaminborbe.website.form.FormEncType;
import de.benjaminborbe.website.form.FormInputFileWidget;
import de.benjaminborbe.website.form.FormInputHiddenWidget;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormMethod;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.ValidationExceptionWidget;

@Singleton
public class GalleryGuiEntryCreateServlet extends GalleryGuiHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Gallery - Upload";

	private final GalleryService galleryService;

	private final Logger logger;

	private final AuthenticationService authenticationService;

	private final ParseUtil parseUtil;

	private final GalleryGuiLinkFactory galleryGuiLinkFactory;

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
			final AuthorizationService authorizationService,
			final GalleryGuiLinkFactory galleryGuiLinkFactory) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.galleryService = galleryService;
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.parseUtil = parseUtil;
		this.galleryGuiLinkFactory = galleryGuiLinkFactory;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createGalleryContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException, LoginRequiredException, SuperAdminRequiredException {

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

					if (containsRequireParameter(parameter) && containsRequireFiles(files)) {
						final FileItem imagePreviewItem = files.get(GalleryGuiConstants.PARAMETER_IMAGE_CONTENT_PREVIEW);
						final String imagePreviewName = imagePreviewItem.getFieldName();
						final byte[] imagePreviewContent = imagePreviewItem.get();
						final String imagePreviewContentType = extractContentType(imagePreviewItem.getContentType(), imagePreviewName);

						final FileItem imageItem = files.get(GalleryGuiConstants.PARAMETER_IMAGE_CONTENT);
						final String imageName = imageItem.getFieldName();
						final byte[] imageContent = imageItem.get();
						final String imageContentType = extractContentType(imageItem.getContentType(), imageName);

						final String galleryId = parameter.get(GalleryGuiConstants.PARAMETER_COLLECTION_ID);
						final String name = parameter.get(GalleryGuiConstants.PARAMETER_ENTRY_NAME);
						final String prio = parameter.get(GalleryGuiConstants.PARAMETER_ENTRY_PRIO);
						final String shared = parameter.get(GalleryGuiConstants.PARAMETER_ENTRY_SHARED);

						final GalleryCollectionIdentifier galleryIdentifier = galleryService.createCollectionIdentifier(galleryId);
						final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
						final GalleryEntryIdentifier entryIdentifier = createEntry(sessionIdentifier, galleryIdentifier, name, prio, imagePreviewName, imagePreviewContent,
								imagePreviewContentType, imageName, imageContent, imageContentType, shared);
						widgets.add("images uploaded!");
						logger.debug("entryIdentifier: " + entryIdentifier);
					}
					else {
						logger.info("parameter missing");
					}

				}
				catch (final FileUploadException e) {
					logger.debug(e.getClass().getName(), e);
					widgets.add(new ExceptionWidget(e));
				}
				catch (final ValidationException e) {
					widgets.add("create entry => failed");
					widgets.add(new ValidationExceptionWidget(e));
				}
			}

			final FormWidget form = new FormWidget().addEncType(FormEncType.MULTIPART).addMethod(FormMethod.POST);
			form.addFormInputWidget(new FormInputHiddenWidget(GalleryGuiConstants.PARAMETER_COLLECTION_ID));
			form.addFormInputWidget(new FormInputTextWidget(GalleryGuiConstants.PARAMETER_ENTRY_NAME).addLabel("Name").addPlaceholder("name..."));
			form.addFormInputWidget(new FormInputTextWidget(GalleryGuiConstants.PARAMETER_ENTRY_PRIO).addLabel("Prio").addPlaceholder("prio..."));
			form.addFormInputWidget(new FormInputTextWidget(GalleryGuiConstants.PARAMETER_ENTRY_SHARED).addLabel("Shared").addPlaceholder("shared...").addDefaultValue("false"));
			form.addFormInputWidget(new FormInputFileWidget(GalleryGuiConstants.PARAMETER_IMAGE_CONTENT_PREVIEW).addLabel("Preview"));
			form.addFormInputWidget(new FormInputFileWidget(GalleryGuiConstants.PARAMETER_IMAGE_CONTENT).addLabel("Image"));
			form.addFormInputWidget(new FormInputSubmitWidget("upload"));
			widgets.add(form);

			final ListWidget navi = new ListWidget();
			navi.add(galleryGuiLinkFactory.listEntries(request, galleryService.createCollectionIdentifier(request.getParameter(GalleryGuiConstants.PARAMETER_COLLECTION_ID))));
			widgets.add(navi);

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

	private boolean containsRequireFiles(final Map<String, FileItem> files) {
		final List<String> parameters = Arrays.asList(GalleryGuiConstants.PARAMETER_IMAGE_CONTENT, GalleryGuiConstants.PARAMETER_IMAGE_CONTENT_PREVIEW);

		for (final String p : parameters) {
			if (!files.containsKey(p)) {
				logger.info("parameter " + p + " missing");
				return false;
			}
		}
		return true;
	}

	private boolean containsRequireParameter(final Map<String, String> parameter) {
		final List<String> parameters = Arrays.asList(GalleryGuiConstants.PARAMETER_COLLECTION_ID, GalleryGuiConstants.PARAMETER_ENTRY_SHARED);

		for (final String p : parameters) {
			if (!parameter.containsKey(p)) {
				logger.info("parameter " + p + " missing");
				return false;
			}
		}
		return true;
	}

	private GalleryEntryIdentifier createEntry(final SessionIdentifier sessionIdentifier, final GalleryCollectionIdentifier galleryIdentifier, final String name,
			final String prioString, final String imagePreviewName, final byte[] imagePreviewContent, final String imagePreviewContentType, final String imageName,
			final byte[] imageContent, final String imageContentType, final String sharedString) throws ValidationException, GalleryServiceException, LoginRequiredException,
			PermissionDeniedException, SuperAdminRequiredException {

		Long prio;
		Boolean shared;
		final List<ValidationError> errors = new ArrayList<ValidationError>();
		{
			try {
				if (prioString == null || prioString.length() == 0) {
					prio = null;
				}
				else {
					prio = parseUtil.parseLong(prioString);
				}
			}
			catch (final ParseException e) {
				prio = null;
				errors.add(new ValidationErrorSimple("illegal prio"));
			}
		}
		{
			try {
				shared = parseUtil.parseBoolean(sharedString);
			}
			catch (final ParseException e) {
				shared = null;
				errors.add(new ValidationErrorSimple("illegal shared"));
			}
		}

		if (!errors.isEmpty()) {
			throw new ValidationException(new ValidationResultImpl(errors));
		}
		else {
			return galleryService.createEntry(sessionIdentifier, galleryIdentifier, name, prio, imagePreviewName, imagePreviewContent, imagePreviewContentType, imageName, imageContent,
					imageContentType, shared);
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
