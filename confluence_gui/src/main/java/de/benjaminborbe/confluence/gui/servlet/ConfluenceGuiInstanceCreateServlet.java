package de.benjaminborbe.confluence.gui.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.confluence.api.ConfluenceInstanceIdentifier;
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
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.validation.ValidationResultImpl;
import de.benjaminborbe.website.form.FormCheckboxWidget;
import de.benjaminborbe.website.form.FormInputHiddenWidget;
import de.benjaminborbe.website.form.FormInputPasswordWidget;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormMethod;
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

	private final ParseUtil parseUtil;

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
			final ConfluenceGuiLinkFactory confluenceGuiLinkFactory,
			final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.confluenceService = confluenceService;
		this.logger = logger;
		this.confluenceGuiLinkFactory = confluenceGuiLinkFactory;
		this.authenticationService = authenticationService;
		this.parseUtil = parseUtil;
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
			final String expire = request.getParameter(ConfluenceGuiConstants.PARAMETER_INSTANCE_EXPIRE);
			final String shared = request.getParameter(ConfluenceGuiConstants.PARAMETER_INSTANCE_SHARED);
			final String activated = request.getParameter(ConfluenceGuiConstants.PARAMETER_INSTANCE_ACTIVATED);
			final String delay = request.getParameter(ConfluenceGuiConstants.PARAMETER_INSTANCE_DELAY);
			final String referer = request.getParameter(ConfluenceGuiConstants.PARAMETER_REFERER);
			final String owner = request.getParameter(ConfluenceGuiConstants.PARAMETER_INSTANCE_OWNER);
			if (url != null && username != null && password != null && expire != null && delay != null && owner != null) {
				try {
					final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

					createConfluenceIntance(sessionIdentifier, url, username, password, expire, shared, delay, activated, owner);

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
			final FormWidget formWidget = new FormWidget().addMethod(FormMethod.POST);
			formWidget.addFormInputWidget(new FormInputHiddenWidget(ConfluenceGuiConstants.PARAMETER_REFERER).addDefaultValue(buildRefererUrl(request)));
			formWidget.addFormInputWidget(new FormInputTextWidget(ConfluenceGuiConstants.PARAMETER_INSTANCE_URL).addLabel("Url:").addPlaceholder("http://..."));
			formWidget.addFormInputWidget(new FormInputTextWidget(ConfluenceGuiConstants.PARAMETER_INSTANCE_USERNAME).addLabel("Username:").addPlaceholder("username..."));
			formWidget.addFormInputWidget(new FormInputPasswordWidget(ConfluenceGuiConstants.PARAMETER_INSTANCE_PASSWORD).addLabel("Password:").addPlaceholder("password..."));
			formWidget.addFormInputWidget(new FormInputTextWidget(ConfluenceGuiConstants.PARAMETER_INSTANCE_EXPIRE).addLabel("Expire in days:").addDefaultValue("7"));
			formWidget.addFormInputWidget(new FormInputTextWidget(ConfluenceGuiConstants.PARAMETER_INSTANCE_DELAY).addLabel("Delay in milliseconds:").addDefaultValue("300"));
			formWidget.addFormInputWidget(new FormCheckboxWidget(ConfluenceGuiConstants.PARAMETER_INSTANCE_SHARED).addLabel("Shared:").setCheckedDefault(true));
			formWidget.addFormInputWidget(new FormCheckboxWidget(ConfluenceGuiConstants.PARAMETER_INSTANCE_ACTIVATED).addLabel("Activated:").setCheckedDefault(true));
			formWidget.addFormInputWidget(new FormInputTextWidget(ConfluenceGuiConstants.PARAMETER_INSTANCE_OWNER).addLabel("Owner:").addPlaceholder("username..."));
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

	private ConfluenceInstanceIdentifier createConfluenceIntance(final SessionIdentifier sessionIdentifier, final String url, final String username, final String password,
			final String expireString, final String sharedString, final String delayString, final String activatedString, final String owner) throws ValidationException,
			ConfluenceServiceException, LoginRequiredException, PermissionDeniedException {
		final List<ValidationError> errors = new ArrayList<ValidationError>();
		int expire = 0;
		{
			try {
				expire = parseUtil.parseInt(expireString);
			}
			catch (final ParseException e) {
				errors.add(new ValidationErrorSimple("illegal expire"));
			}
		}

		long delay = 0;
		{
			try {
				delay = parseUtil.parseInt(delayString);
			}
			catch (final ParseException e) {
				errors.add(new ValidationErrorSimple("illegal delay"));
			}
		}

		final boolean shared = parseUtil.parseBoolean(sharedString, false);
		final boolean activated = parseUtil.parseBoolean(activatedString, false);

		if (!errors.isEmpty()) {
			throw new ValidationException(new ValidationResultImpl(errors));
		}
		else {
			return confluenceService.createConfluenceIntance(sessionIdentifier, url, username, password, expire, shared, delay, activated, owner);
		}
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}
}
