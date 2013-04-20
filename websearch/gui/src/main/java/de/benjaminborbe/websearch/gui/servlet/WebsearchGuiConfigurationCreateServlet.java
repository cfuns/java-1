package de.benjaminborbe.websearch.gui.servlet;

import javax.inject.Inject;
import com.google.inject.Provider;
import javax.inject.Singleton;
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
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.validation.ValidationResultImpl;
import de.benjaminborbe.websearch.api.WebsearchConfigurationIdentifier;
import de.benjaminborbe.websearch.api.WebsearchService;
import de.benjaminborbe.websearch.api.WebsearchServiceException;
import de.benjaminborbe.websearch.gui.WebsearchGuiConstants;
import de.benjaminborbe.websearch.gui.util.WebsearchGuiLinkFactory;
import de.benjaminborbe.website.form.FormCheckboxWidget;
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
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class WebsearchGuiConfigurationCreateServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = -5013723680643328782L;

	private static final String TITLE = "Websearch - Create";

	private final WebsearchService websearchService;

	private final Logger logger;

	private final WebsearchGuiLinkFactory websearchGuiLinkFactory;

	private final AuthenticationService authenticationService;

	private final ParseUtil parseUtil;

	@Inject
	public WebsearchGuiConfigurationCreateServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final NavigationWidget navigationWidget,
		final AuthenticationService authenticationService,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final WebsearchService websearchService,
		final AuthorizationService authorizationService,
		final WebsearchGuiLinkFactory websearchGuiLinkFactory,
		final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.websearchService = websearchService;
		this.logger = logger;
		this.websearchGuiLinkFactory = websearchGuiLinkFactory;
		this.authenticationService = authenticationService;
		this.parseUtil = parseUtil;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
		PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));
			final String url = request.getParameter(WebsearchGuiConstants.PARAMETER_CONFIGURATION_URL);
			final String excludes = request.getParameter(WebsearchGuiConstants.PARAMETER_CONFIGURATION_EXCLUDES);
			final String expire = request.getParameter(WebsearchGuiConstants.PARAMETER_CONFIGURATION_EXPIRE);
			final String delay = request.getParameter(WebsearchGuiConstants.PARAMETER_CONFIGURATION_DELAY);
			final String activated = request.getParameter(WebsearchGuiConstants.PARAMETER_CONFIGURATION_ACTIVATED);
			final String referer = request.getParameter(WebsearchGuiConstants.PARAMETER_REFERER);
			if (url != null && excludes != null && expire != null && delay != null) {
				try {
					final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

					createConfiguration(sessionIdentifier, url, excludes, expire, delay, activated);

					if (referer != null) {
						throw new RedirectException(referer);
					} else {
						throw new RedirectException(websearchGuiLinkFactory.configurationListUrl(request));
					}
				} catch (final ValidationException e) {
					widgets.add("create configuration => failed");
					widgets.add(new ValidationExceptionWidget(e));
				}
			}
			final FormWidget formWidget = new FormWidget();
			formWidget.addFormInputWidget(new FormInputHiddenWidget(WebsearchGuiConstants.PARAMETER_REFERER).addDefaultValue(buildRefererUrl(request)));
			formWidget.addFormInputWidget(new FormInputTextWidget(WebsearchGuiConstants.PARAMETER_CONFIGURATION_URL).addLabel("Url:").addPlaceholder("http://..."));
			formWidget.addFormInputWidget(new FormInputTextWidget(WebsearchGuiConstants.PARAMETER_CONFIGURATION_EXCLUDES).addLabel("Excludes:").addDefaultValue("?"));
			formWidget.addFormInputWidget(new FormInputTextWidget(WebsearchGuiConstants.PARAMETER_CONFIGURATION_EXPIRE).addLabel("Expire in days:").addDefaultValue("7"));
			formWidget.addFormInputWidget(new FormInputTextWidget(WebsearchGuiConstants.PARAMETER_CONFIGURATION_DELAY).addLabel("Delay in milliseconds:").addDefaultValue("300"));
			formWidget.addFormInputWidget(new FormCheckboxWidget(WebsearchGuiConstants.PARAMETER_CONFIGURATION_ACTIVATED).addLabel("Activated:").setCheckedDefault(true));
			formWidget.addFormInputWidget(new FormInputSubmitWidget("create"));
			widgets.add(formWidget);
			return widgets;
		} catch (final WebsearchServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return new ExceptionWidget(e);
		} catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return new ExceptionWidget(e);
		}
	}

	private WebsearchConfigurationIdentifier createConfiguration(final SessionIdentifier sessionIdentifier, final String urlString, final String excludesString,
																															 final String expireString, final String delayString, final String activatedString) throws WebsearchServiceException, LoginRequiredException, PermissionDeniedException,
		ValidationException {
		final List<ValidationError> errors = new ArrayList<>();
		URL url;
		{
			try {
				url = parseUtil.parseURL(urlString);
			} catch (final ParseException e) {
				url = null;
				errors.add(new ValidationErrorSimple("illegal url"));
			}
		}

		final List<String> excludes = new ArrayList<>();
		{
			if (excludesString != null) {
				final String[] parts = excludesString.split(",");
				for (final String part : parts) {
					if (part != null && part.trim().length() > 0) {
						excludes.add(part.trim());
					}
				}
			} else {
				errors.add(new ValidationErrorSimple("illegal excludes"));
			}
		}

		int expire = 0;
		{
			try {
				expire = parseUtil.parseInt(expireString);
			} catch (final ParseException e) {
				errors.add(new ValidationErrorSimple("illegal expire"));
			}
		}

		long delay = 0;
		{
			try {
				delay = parseUtil.parseInt(delayString);
			} catch (final ParseException e) {
				errors.add(new ValidationErrorSimple("illegal delay"));
			}
		}

		final boolean activated = parseUtil.parseBoolean(activatedString, false);

		if (!errors.isEmpty()) {
			throw new ValidationException(new ValidationResultImpl(errors));
		} else {
			return websearchService.createConfiguration(sessionIdentifier, url, excludes, expire, delay, activated);
		}
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}
}
