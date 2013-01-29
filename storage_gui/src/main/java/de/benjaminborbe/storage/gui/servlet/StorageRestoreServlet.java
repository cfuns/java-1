package de.benjaminborbe.storage.gui.servlet;

import java.io.IOException;
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

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.gui.StorageGuiConstants;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.form.FormEncType;
import de.benjaminborbe.website.form.FormInputFileWidget;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormMethod;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;

@Singleton
public class StorageRestoreServlet extends StorageHtmlServlet {

	private static final long serialVersionUID = 1048276599809672509L;

	private static final String TITLE = "Storage - Backup";

	private final Logger logger;

	private final StorageService storageService;

	@Inject
	public StorageRestoreServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final Provider<HttpContext> httpContextProvider,
			final UrlUtil urlUtil,
			final StorageService storageService,
			final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.storageService = storageService;
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

					if (containsRequireParameter(parameter) && containsRequireFiles(files)) {
						final String columnfamily = parameter.get(StorageGuiConstants.PARAMETER_COLUMNFAMILY);
						final FileItem item = files.get(StorageGuiConstants.PARAMETER_RESTORE_JSON);
						final byte[] content = item.get();
						storageService.restore(columnfamily, new String(content, "UTF-8"));
						widgets.add("json uploaded!");
					}
					else {
						logger.info("parameter missing");
					}
				}
				catch (final FileUploadException e) {
					logger.debug(e.getClass().getName(), e);
					widgets.add(new ExceptionWidget(e));
				}
			}

			final FormWidget form = new FormWidget().addEncType(FormEncType.MULTIPART).addMethod(FormMethod.POST);
			form.addFormInputWidget(new FormInputTextWidget(StorageGuiConstants.PARAMETER_COLUMNFAMILY).addLabel("ColumnFamily"));
			form.addFormInputWidget(new FormInputFileWidget(StorageGuiConstants.PARAMETER_RESTORE_JSON).addLabel("Json"));
			form.addFormInputWidget(new FormInputSubmitWidget("upload"));
			widgets.add(form);

			return widgets;
		}
		catch (final StorageException e) {
			return new ExceptionWidget(e);
		}
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	private boolean containsRequireFiles(final Map<String, FileItem> files) {
		final List<String> parameters = Arrays.asList(StorageGuiConstants.PARAMETER_RESTORE_JSON);

		for (final String p : parameters) {
			if (!files.containsKey(p)) {
				logger.info("parameter " + p + " missing");
				return false;
			}
		}
		return true;
	}

	private boolean containsRequireParameter(final Map<String, String> parameter) {
		final List<String> parameters = Arrays.asList(StorageGuiConstants.PARAMETER_COLUMNFAMILY);

		for (final String p : parameters) {
			if (!parameter.containsKey(p)) {
				logger.info("parameter " + p + " missing");
				return false;
			}
		}
		return true;
	}
}
