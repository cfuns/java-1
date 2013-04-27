package de.benjaminborbe.lunch.gui.servlet;

import com.google.inject.Provider;
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
import de.benjaminborbe.lunch.api.Lunch;
import de.benjaminborbe.lunch.api.LunchServiceException;
import de.benjaminborbe.lunch.gui.LunchGuiConstants;
import de.benjaminborbe.lunch.gui.util.LunchGuiComparator;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.DateUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormMethod;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.link.LinkWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.LiWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.TagWidget;
import de.benjaminborbe.website.util.UlWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;

@Singleton
public abstract class LunchGuiBaseServlet extends LunchGuiHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Lunch";

	private final DateUtil dateUtil;

	private final Logger logger;

	private final AuthenticationService authenticationService;

	@Inject
	public LunchGuiBaseServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final AuthenticationService authenticationService,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final DateUtil dateUtil,
		final AuthorizationService authorizationService,
		final CacheService cacheService
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.dateUtil = dateUtil;
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
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);

			String fullname = null;
			if (fullname == null) {
				fullname = request.getParameter(LunchGuiConstants.PARAMETER_FULLNAME);
			}
			if (fullname == null) {
				fullname = authenticationService.getFullname(userIdentifier);
			}
			if (fullname != null) {
				final List<Lunch> lunchs = getLunchs(sessionIdentifier, fullname);
				Collections.sort(lunchs, new LunchGuiComparator());
				final UlWidget ul = new UlWidget();
				for (final Lunch lunch : lunchs) {
					final StringWriter content = new StringWriter();
					content.append(dateUtil.dateString(lunch.getDate()));
					content.append(" ");
					content.append(lunch.getName());
					content.append(" (");
					content.append((lunch.isSubscribed() ? "subscribed" : "not subscribed"));
					content.append(")");

					final LiWidget li;
					if (dateUtil.isToday(lunch.getDate())) {
						li = new LiWidget(new LinkWidget(lunch.getUrl(), new TagWidget("b", content.toString())));
					} else {
						li = new LiWidget(new LinkWidget(lunch.getUrl(), content.toString()));
					}

					li.addAttribute("class", (lunch.isSubscribed() ? "subscribed" : "notsubscribed"));

					ul.add(li);
				}
				widgets.add(ul);
			}

			final FormWidget formWidget = new FormWidget().addMethod(FormMethod.POST);
			formWidget.addFormInputWidget(new FormInputTextWidget(LunchGuiConstants.PARAMETER_FULLNAME).addLabel("Vor und Nachname").addPlaceholder("name..."));
			formWidget.addFormInputWidget(new FormInputSubmitWidget("search"));
			widgets.add(formWidget);

			return widgets;
		} catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		} catch (final LunchServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	protected abstract List<Lunch> getLunchs(
		final SessionIdentifier sessionIdentifier,
		final String fullname
	) throws LunchServiceException, LoginRequiredException,
		PermissionDeniedException;

}
