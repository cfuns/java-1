package de.benjaminborbe.microblog.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.microblog.api.MicroblogService;
import de.benjaminborbe.microblog.api.MicroblogServiceException;
import de.benjaminborbe.microblog.gui.MicroblogGuiConstants;
import de.benjaminborbe.microblog.gui.util.MicroblogGuiLinkFactory;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ComparatorStringCaseInsensitive;
import de.benjaminborbe.tools.util.ComparatorUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormMethod;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;
import de.benjaminborbe.website.widget.ValidationExceptionWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;

@Singleton
public class MicroblogGuiNotificationListServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 3626403758568366150L;

	private static final String TITLE = "Microblog - Notification";

	private final Logger logger;

	private final AuthenticationService authenticationService;

	private final MicroblogService microblogService;

	private final ComparatorUtil comparatorUtil;

	private final Comparator<String> comparatorStringCaseInsensitive;

	private final MicroblogGuiLinkFactory microblogGuiLinkFactory;

	@Inject
	public MicroblogGuiNotificationListServlet(
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
		final MicroblogService microblogService,
		final ComparatorUtil comparatorUtil,
		final ComparatorStringCaseInsensitive comparatorStringCaseInsensitive,
		final MicroblogGuiLinkFactory microblogGuiLinkFactory
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.microblogService = microblogService;
		this.comparatorUtil = comparatorUtil;
		this.comparatorStringCaseInsensitive = comparatorStringCaseInsensitive;
		this.microblogGuiLinkFactory = microblogGuiLinkFactory;
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
			logger.trace("printContent");
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);

			{
				final String keyword = request.getParameter(MicroblogGuiConstants.PARAEMTER_NOTIFICATION_KEYWORD);
				if (keyword != null && !keyword.isEmpty()) {
					try {
						microblogService.activateNotification(userIdentifier, keyword);
					} catch (final ValidationException e) {
						widgets.add("add keyword failed");
						widgets.add(new ValidationExceptionWidget(e));
					}
				}
			}

			widgets.add(new H1Widget(getTitle()));
			final Collection<String> keywords = comparatorUtil.sort(microblogService.listNotifications(userIdentifier), comparatorStringCaseInsensitive);
			final UlWidget ul = new UlWidget();
			for (final String keyword : keywords) {
				final ListWidget row = new ListWidget();
				row.add(keyword);
				row.add(" ");
				row.add(microblogGuiLinkFactory.deleteNotification(request, userIdentifier, keyword));
				ul.add(row);
			}
			widgets.add(ul);
			final FormWidget form = new FormWidget().addMethod(FormMethod.POST);
			form.addFormInputWidget(new FormInputTextWidget(MicroblogGuiConstants.PARAEMTER_NOTIFICATION_KEYWORD).addLabel("Keyword:"));
			form.addFormInputWidget(new FormInputSubmitWidget("add"));
			widgets.add(form);

			return widgets;
		} catch (final MicroblogServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		} catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}
}
