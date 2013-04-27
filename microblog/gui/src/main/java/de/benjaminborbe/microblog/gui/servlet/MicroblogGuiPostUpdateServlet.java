package de.benjaminborbe.microblog.gui.servlet;

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
import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.microblog.api.MicroblogService;
import de.benjaminborbe.microblog.api.MicroblogServiceException;
import de.benjaminborbe.microblog.gui.MicroblogGuiConstants;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormMethod;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.BrWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class MicroblogGuiPostUpdateServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Microblog - Update Post";

	private final MicroblogService microblogService;

	private final Logger logger;

	private final AuthenticationService authenticationService;

	private final ParseUtil parseUtil;

	@Inject
	public MicroblogGuiPostUpdateServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final AuthenticationService authenticationService,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final MicroblogService microblogService,
		final UrlUtil urlUtil,
		final AuthorizationService authorizationService,
		final CacheService cacheService
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.parseUtil = parseUtil;
		this.microblogService = microblogService;
		this.authenticationService = authenticationService;
		this.logger = logger;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
		PermissionDeniedException, LoginRequiredException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

			final Long postId = parseLong(request.getParameter(MicroblogGuiConstants.PARAMETER_POST_ID));
			if (postId != null) {
				final MicroblogPostIdentifier postIdent = microblogService.createMicroblogPostIdentifier(postId);
				microblogService.updatePost(sessionIdentifier, postIdent);
				widgets.add("post updated");
				widgets.add(new BrWidget());
			}

			final FormWidget form = new FormWidget().addMethod(FormMethod.POST);
			form.addFormInputWidget(new FormInputTextWidget(MicroblogGuiConstants.PARAMETER_POST_ID).addLabel("PostId:").addDefaultValue(microblogService.getLatestPostIdentifier()));
			form.addFormInputWidget(new FormInputSubmitWidget("updatePost"));
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

	private Long parseLong(final String number) {
		try {
			return parseUtil.parseLong(number);
		} catch (final ParseException e) {
			logger.debug("parse number " + number + " failed");
			return null;
		}
	}
}
