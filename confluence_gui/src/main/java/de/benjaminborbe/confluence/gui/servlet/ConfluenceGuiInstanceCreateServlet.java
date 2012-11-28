package de.benjaminborbe.confluence.gui.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

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
import de.benjaminborbe.confluence.api.ConfluenceService;
import de.benjaminborbe.confluence.api.ConfluenceServiceException;
import de.benjaminborbe.confluence.gui.ConfluenceGuiConstants;
import de.benjaminborbe.confluence.gui.util.ConfluenceGuiLinkFactory;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.form.FormInputHiddenWidget;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.ValidationExceptionWidget;

@Singleton
public class ConfluenceGuiInstanceCreateServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = -5013723680643328782L;

	private static final String TITLE = "Confluence - Instance - Create";

	private final ConfluenceService confluenceService;

	private final Logger logger;

	private final ConfluenceGuiLinkFactory confluenceGuiLinkFactory;

	private final AuthenticationService authenticationService;

	@Inject
	public ConfluenceGuiInstanceCreateServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final AuthenticationService authenticationService,
			final Provider<HttpContext> httpContextProvider,
			final UrlUtil urlUtil,
			final ConfluenceService confluenceService,
			final AuthorizationService authorizationService,
			final ConfluenceGuiLinkFactory confluenceGuiLinkFactory) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.confluenceService = confluenceService;
		this.logger = logger;
		this.confluenceGuiLinkFactory = confluenceGuiLinkFactory;
		this.authenticationService = authenticationService;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));
			final String url = request.getParameter(ConfluenceGuiConstants.PARAMETER_INSTANCE_URL);
			final String username = request.getParameter(ConfluenceGuiConstants.PARAMETER_INSTANCE_USERNAME);
			final String password = request.getParameter(ConfluenceGuiConstants.PARAMETER_INSTANCE_PASSWORD);
			final String referer = request.getParameter(ConfluenceGuiConstants.PARAMETER_REFERER);
			if (url != null && username != null && password != null) {
				try {
					final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

					confluenceService.createConfluenceIntance(sessionIdentifier, url, username, password);

					if (referer != null) {
						throw new RedirectException(referer);
					}
					else {
						throw new RedirectException(confluenceGuiLinkFactory.instanceListUrl(request));
					}
				}
				catch (final ValidationException e) {
					widgets.add("create instance => failed");
					widgets.add(new ValidationExceptionWidget(e));
				}
			}
			final FormWidget formWidget = new FormWidget();
			formWidget.addFormInputWidget(new FormInputHiddenWidget(ConfluenceGuiConstants.PARAMETER_REFERER).addDefaultValue(buildRefererUrl(request)));
			formWidget.addFormInputWidget(new FormInputTextWidget(ConfluenceGuiConstants.PARAMETER_INSTANCE_URL).addLabel("Url:").addPlaceholder("http://..."));
			formWidget.addFormInputWidget(new FormInputTextWidget(ConfluenceGuiConstants.PARAMETER_INSTANCE_USERNAME).addLabel("Username:").addPlaceholder("username..."));
			formWidget.addFormInputWidget(new FormInputTextWidget(ConfluenceGuiConstants.PARAMETER_INSTANCE_PASSWORD).addLabel("Password:").addPlaceholder("password..."));
			formWidget.addFormInputWidget(new FormInputSubmitWidget("create"));
			widgets.add(formWidget);
			return widgets;
		}
		catch (final ConfluenceServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return new ExceptionWidget(e);
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return new ExceptionWidget(e);
		}
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}
}
