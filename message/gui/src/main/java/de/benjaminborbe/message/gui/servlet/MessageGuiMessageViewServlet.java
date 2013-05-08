package de.benjaminborbe.message.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.message.api.Message;
import de.benjaminborbe.message.api.MessageIdentifier;
import de.benjaminborbe.message.api.MessageService;
import de.benjaminborbe.message.api.MessageServiceException;
import de.benjaminborbe.message.gui.MessageGuiConstants;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.table.TableRowWidget;
import de.benjaminborbe.website.table.TableWidget;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.PreWidget;
import de.benjaminborbe.website.widget.BrWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;

@Singleton
public class MessageGuiMessageViewServlet extends WebsiteHtmlServlet {

	public static final String MESSAGE_VIEW = "Message - View";

	public static final String EMPTY_VALUE = "-";

	private final Logger logger;

	private final CalendarUtil calendarUtil;

	private final MessageService messageService;

	private final AuthenticationService authenticationService;

	@Inject
	public MessageGuiMessageViewServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final NavigationWidget navigationWidget,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final CacheService cacheService,
		final MessageService messageService
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.calendarUtil = calendarUtil;
		this.messageService = messageService;
		this.authenticationService = authenticationService;
	}

	@Override
	protected String getTitle() {
		return MESSAGE_VIEW;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
		PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			String messageId = request.getParameter(MessageGuiConstants.PARAMETER_ID);
			final Message message = messageService.getMessage(sessionIdentifier, messageService.createMessageIdentifier(messageId));
			final TableWidget table = new TableWidget();
			table.addRow(new TableRowWidget().addCell("Id: ").addCell(toString(message.getId())));
			table.addRow(new TableRowWidget().addCell("Type: ").addCell(toString(message.getType())));
			table.addRow(new TableRowWidget().addCell("LockName: ").addCell(toString(message.getLockName())));
			table.addRow(new TableRowWidget().addCell("LockTime: ").addCell(toString(message.getLockTime())));
			table.addRow(new TableRowWidget().addCell("StartTime: ").addCell(toString(message.getStartTime())));
			table.addRow(new TableRowWidget().addCell("Created: ").addCell(toString(message.getCreated())));
			table.addRow(new TableRowWidget().addCell("Modified: ").addCell(toString(message.getModified())));
			table.addRow(new TableRowWidget().addCell("MaxRetryCounter: ").addCell(toString(message.getMaxRetryCounter())));
			table.addRow(new TableRowWidget().addCell("RetryCounter: ").addCell(toString(message.getRetryCounter())));
			widgets.add(table);
			widgets.add("Content:");
			widgets.add(new BrWidget());
			widgets.add(new PreWidget(message.getContent()));
			return widgets;
		} catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		} catch (final MessageServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	private String toString(final String string) {
		return string != null ? string : EMPTY_VALUE;
	}

	private String toString(final Long aLong) {
		return aLong != null ? String.valueOf(aLong) : EMPTY_VALUE;
	}

	private String toString(final MessageIdentifier messageIdentifier) {
		return messageIdentifier != null ? messageIdentifier.getId() : EMPTY_VALUE;
	}

	private String toString(final Calendar calendar) {
		return calendar != null ? calendarUtil.toDateTimeString(calendar) : EMPTY_VALUE;
	}
}