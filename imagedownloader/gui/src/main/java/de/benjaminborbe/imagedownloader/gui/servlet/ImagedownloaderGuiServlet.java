package de.benjaminborbe.imagedownloader.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.imagedownloader.api.ImagedownloaderService;
import de.benjaminborbe.imagedownloader.api.ImagedownloaderServiceException;
import de.benjaminborbe.imagedownloader.gui.ImagedownloaderGuiConstants;
import de.benjaminborbe.lib.validation.ValidationResultImpl;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
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

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ImagedownloaderGuiServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Imagedownloader";

	private final ParseUtil parseUtil;

	private final ImagedownloaderService imagedownloaderService;

	@Inject
	public ImagedownloaderGuiServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final AuthenticationService authenticationService,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final AuthorizationService authorizationService,
		final CacheService cacheService,
		final ImagedownloaderService imagedownloaderService
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil,
			cacheService);
		this.parseUtil = parseUtil;
		this.imagedownloaderService = imagedownloaderService;
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
			widgets.add(new H1Widget(getTitle()));

			final String url = request.getParameter(ImagedownloaderGuiConstants.PARAMETER_URL);
			final String depth = request.getParameter(ImagedownloaderGuiConstants.PARAMETER_DEPTH);
			final String minWidth = request.getParameter(ImagedownloaderGuiConstants.PARAMETER_MIN_WIDTH);
			final String minHeight = request.getParameter(ImagedownloaderGuiConstants.PARAMETER_MIN_HEIGHT);
			if (url != null && depth != null && minWidth != null && minHeight != null) {
				try {
					action(url, depth, minWidth, minHeight);
					widgets.add("downloaded images successful");
				} catch (ValidationException e) {
					widgets.add("downloaded images failed!");
					widgets.add(new ValidationExceptionWidget(e));
				}
			}

			final FormWidget form = new FormWidget();
			form.addFormInputWidget(new FormInputTextWidget(ImagedownloaderGuiConstants.PARAMETER_URL).addLabel("Url:"));
			form.addFormInputWidget(new FormInputTextWidget(ImagedownloaderGuiConstants.PARAMETER_DEPTH).addLabel("Depth:").addDefaultValue("0"));
			form.addFormInputWidget(new FormInputTextWidget(ImagedownloaderGuiConstants.PARAMETER_MIN_WIDTH).addLabel("Min-Width:").addDefaultValue("0"));
			form.addFormInputWidget(new FormInputTextWidget(ImagedownloaderGuiConstants.PARAMETER_MIN_HEIGHT).addLabel("Min-Height:").addDefaultValue("0"));
			form.addFormInputWidget(new FormInputSubmitWidget("download"));
			widgets.add(form);

			return widgets;
		} catch (final ImagedownloaderServiceException e) {
			return new ExceptionWidget(e);
		}
	}

	private void action(
		final String urlString,
		final String depthString,
		final String minWidthString,
		final String minHeightString
	) throws ValidationException, ImagedownloaderServiceException {
		final List<ValidationError> errors = new ArrayList<ValidationError>();
		URL url = null;
		{
			try {
				url = parseUtil.parseURL(urlString);
			} catch (final ParseException e) {
				errors.add(new ValidationErrorSimple("illegal url"));
			}
		}

		int depth = 0;
		{
			try {
				depth = parseUtil.parseInt(depthString);
			} catch (final ParseException e) {
				errors.add(new ValidationErrorSimple("illegal depth"));
			}
		}

		int minHeight = 0;
		{
			try {
				minHeight = parseUtil.parseInt(minHeightString);
			} catch (final ParseException e) {
				errors.add(new ValidationErrorSimple("illegal min height"));
			}
		}

		int minWidth = 0;
		{
			try {
				minWidth = parseUtil.parseInt(minWidthString);
			} catch (final ParseException e) {
				errors.add(new ValidationErrorSimple("illegal min width"));
			}
		}

		if (!errors.isEmpty()) {
			throw new ValidationException(new ValidationResultImpl(errors));
		} else {
			imagedownloaderService.downloadImages(url, depth, minWidth, minHeight);
		}
	}
}
