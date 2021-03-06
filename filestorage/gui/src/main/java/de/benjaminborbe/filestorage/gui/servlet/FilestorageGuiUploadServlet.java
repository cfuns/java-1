package de.benjaminborbe.filestorage.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.filestorage.api.FilestorageEntryDto;
import de.benjaminborbe.filestorage.api.FilestorageEntryIdentifier;
import de.benjaminborbe.filestorage.api.FilestorageService;
import de.benjaminborbe.filestorage.api.FilestorageServiceException;
import de.benjaminborbe.filestorage.gui.util.FilestorageGuiLinkFactory;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.form.FormEncType;
import de.benjaminborbe.website.form.FormInputFileWidget;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormMethod;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.BrWidget;
import de.benjaminborbe.website.widget.ValidationExceptionWidget;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Singleton
public class FilestorageGuiUploadServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Filestorage";

	private final Logger logger;

	private final FilestorageService filestorageService;

	private final FilestorageGuiLinkFactory filestorageGuiLinkFactory;

	@Inject
	public FilestorageGuiUploadServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final AuthenticationService authenticationService,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final AuthorizationService authorizationService,
		final CacheService cacheService, final FilestorageService filestorageService, final FilestorageGuiLinkFactory filestorageGuiLinkFactory
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.filestorageService = filestorageService;
		this.filestorageGuiLinkFactory = filestorageGuiLinkFactory;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
		PermissionDeniedException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

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
						widgets.add("file " + item.getName() + " uploaded!");
						widgets.add(filestorageGuiLinkFactory.downloadLink(request, handleUpload(item)));
						widgets.add(new BrWidget());
					}
				} catch (final FileUploadException e) {
					widgets.add("file upload failed");
				} catch (ValidationException e) {
					widgets.add("file upload failed");
					widgets.add(new ValidationExceptionWidget(e));
				}
			}
			final FormWidget formWidget = new FormWidget(request.getContextPath() + "/filestorage/upload");
			formWidget.addMethod(FormMethod.POST);
			formWidget.addEncType(FormEncType.MULTIPART);
			formWidget.addFormInputWidget(new FormInputFileWidget("file").addLabel("File"));
			formWidget.addFormInputWidget(new FormInputSubmitWidget("upload"));
			widgets.add(formWidget);

			return widgets;
		} catch (FilestorageServiceException e) {
			return new ExceptionWidget(e);
		}
	}

	protected FilestorageEntryIdentifier handleUpload(final FileItem item) throws FilestorageServiceException, ValidationException {
		final FilestorageEntryDto filestorageEntry = new FilestorageEntryDto();
		filestorageEntry.setFilename(item.getFieldName());
		filestorageEntry.setContentType(item.getContentType());
		filestorageEntry.setContent(item.get());
		return filestorageService.createFilestorageEntry(filestorageEntry);
	}
}
