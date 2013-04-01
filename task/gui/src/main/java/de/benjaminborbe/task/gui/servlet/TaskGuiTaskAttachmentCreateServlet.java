package de.benjaminborbe.task.gui.servlet;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.task.api.TaskAttachmentWithContentDto;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.api.TaskServiceException;
import de.benjaminborbe.task.gui.TaskGuiConstants;
import de.benjaminborbe.task.gui.util.TaskGuiLinkFactory;
import de.benjaminborbe.task.gui.util.TaskGuiUtil;
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
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class TaskGuiTaskAttachmentCreateServlet extends TaskGuiWebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "TaskAttachment - Create";

	private final Logger logger;

	private final TaskService taskService;

	private final AuthenticationService authenticationService;

	private final TaskGuiLinkFactory taskGuiLinkFactory;

	@Inject
	public TaskGuiTaskAttachmentCreateServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final AuthenticationService authenticationService,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final AuthorizationService authorizationService,
		final TaskService taskService,
		final TaskGuiLinkFactory taskGuiLinkFactory,
		final TaskGuiUtil taskGuiUtil,
		final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, taskGuiUtil, cacheService);
		this.logger = logger;
		this.taskService = taskService;
		this.authenticationService = authenticationService;
		this.taskGuiLinkFactory = taskGuiLinkFactory;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createTaskContentWidget(final HttpServletRequest request) throws IOException,
		PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final Map<String, FileItem> files = new HashMap<String, FileItem>();
			final Map<String, String> parameter = new HashMap<String, String>();

			final boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if (isMultipart) {
				// Create a factory for disk-based file items
				final FileItemFactory factory = new DiskFileItemFactory();

				// Create a new file upload handler
				final ServletFileUpload upload = new ServletFileUpload(factory);

				// Parse the request
				try {
					final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

					@SuppressWarnings("unchecked")
					final List<FileItem> items = upload.parseRequest(request);

					for (final FileItem item : items) {
						final String itemName = item.getFieldName();
						if (!item.isFormField()) {
							logger.info("found image: '" + itemName + "'");
							files.put(itemName, item);
						} else {
							logger.info("parameter " + itemName + " = " + item.getString());
							parameter.put(itemName, item.getString());
						}
					}

					for (final FileItem item : files.values()) {
						final TaskIdentifier taskIdentifier = taskService.createTaskIdentifier(parameter.get(TaskGuiConstants.PARAMETER_TASK_ID));
						TaskAttachmentWithContentDto taskAttachment = new TaskAttachmentWithContentDto();
						taskAttachment.setName(parameter.get(TaskGuiConstants.PARAMETER_TASKATTACHMENT_NAME));
						taskAttachment.setTask(taskIdentifier);
						taskAttachment.setContent(item.get());
						taskAttachment.setFilename(item.getFieldName());
						taskAttachment.setContentType(buildContentType(item));
						logger.debug("name: " + taskAttachment.getName());
						logger.debug("filename: " + taskAttachment.getFilename());
						logger.debug("contentType: " + taskAttachment.getContentType());
						taskService.addAttachment(sessionIdentifier, taskAttachment);

						widgets.add("file " + item.getName() + " uploaded!");
						widgets.add(new BrWidget());

						final String referer = parameter.get(TaskGuiConstants.PARAMETER_REFERER);
						if (referer != null) {
							throw new RedirectException(referer);
						} else {
							throw new RedirectException(taskGuiLinkFactory.taskViewUrl(request, taskIdentifier));
						}
					}
				} catch (final FileUploadException | SAXException | TikaException e) {
					widgets.add("file upload failed");
				} catch (ValidationException e) {
					widgets.add("file upload failed");
					widgets.add(new ValidationExceptionWidget(e));
				}
			}

			final FormWidget formWidget = new FormWidget().addMethod(FormMethod.POST);
			formWidget.addEncType(FormEncType.MULTIPART);
			formWidget.addFormInputWidget(new FormInputHiddenWidget(TaskGuiConstants.PARAMETER_REFERER).addDefaultValue(buildRefererUrl(request)));
			formWidget.addFormInputWidget(new FormInputHiddenWidget(TaskGuiConstants.PARAMETER_TASK_ID));
			formWidget.addFormInputWidget(new FormInputTextWidget(TaskGuiConstants.PARAMETER_TASKATTACHMENT_NAME).addLabel("Name").addPlaceholder("name..."));
			formWidget.addFormInputWidget(new FormInputFileWidget(TaskGuiConstants.PARAMETER_TASKATTACHMENT_CONTENT).addLabel("Attachment"));
			formWidget.addFormInputWidget(new FormInputSubmitWidget("add attachment"));
			widgets.add(formWidget);

			final ListWidget links = new ListWidget();
			links.add(taskGuiLinkFactory.tasksNext(request));
			links.add(" ");
			links.add(taskGuiLinkFactory.tasksUncompleted(request));
			links.add(" ");
			links.add(taskGuiLinkFactory.taskContextList(request));
			widgets.add(links);

			return widgets;
		} catch (final AuthenticationServiceException e) {
			logger.trace(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		} catch (final TaskServiceException e) {
			logger.trace(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	private String buildContentType(final FileItem item) throws IOException, TikaException, SAXException {
		if (item.getContentType() != null) {
			return item.getContentType();
		}
		Parser parser = new AutoDetectParser();
		final InputStream inputStream = item.getInputStream();
		final ContentHandler contentHandler = new BodyContentHandler();
		final Metadata metadata = new Metadata();
		metadata.set(Metadata.RESOURCE_NAME_KEY, item.getFieldName());
		final ParseContext parseContext = new ParseContext();
		parser.parse(inputStream, contentHandler, metadata, parseContext);
		return metadata.get(Metadata.CONTENT_TYPE);
	}
}
