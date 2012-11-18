package de.benjaminborbe.gallery.gui.servlet;

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
import de.benjaminborbe.gallery.api.GalleryGroupIdentifier;
import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.gallery.api.GalleryServiceException;
import de.benjaminborbe.gallery.gui.GalleryGuiConstants;
import de.benjaminborbe.gallery.gui.util.GalleryGuiLinkFactory;
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
public class GalleryGuiGroupCreateServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = -5013723680643328782L;

	private static final String TITLE = "Gallery - Create";

	private final GalleryService galleryService;

	private final Logger logger;

	private final GalleryGuiLinkFactory galleryGuiLinkFactory;

	private final AuthenticationService authenticationService;

	@Inject
	public GalleryGuiGroupCreateServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final AuthenticationService authenticationService,
			final Provider<HttpContext> httpContextProvider,
			final UrlUtil urlUtil,
			final GalleryService galleryService,
			final AuthorizationService authorizationService,
			final GalleryGuiLinkFactory galleryGuiLinkFactory) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.galleryService = galleryService;
		this.logger = logger;
		this.galleryGuiLinkFactory = galleryGuiLinkFactory;
		this.authenticationService = authenticationService;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));
			final String name = request.getParameter(GalleryGuiConstants.PARAMETER_GROUP_NAME);
			final String referer = request.getParameter(GalleryGuiConstants.PARAMETER_REFERER);
			if (name != null) {
				try {
					final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

					final GalleryGroupIdentifier galleryGroupIdentifier = createGroup(sessionIdentifier, name);

					if (referer != null) {
						throw new RedirectException(referer);
					}
					else {
						throw new RedirectException(galleryGuiLinkFactory.collectionListUrl(request, galleryGroupIdentifier));
					}
				}
				catch (final ValidationException e) {
					widgets.add("create collection => failed");
					widgets.add(new ValidationExceptionWidget(e));
				}
			}
			final FormWidget formWidget = new FormWidget();
			formWidget.addFormInputWidget(new FormInputHiddenWidget(GalleryGuiConstants.PARAMETER_REFERER).addDefaultValue(buildRefererUrl(request)));
			formWidget.addFormInputWidget(new FormInputTextWidget(GalleryGuiConstants.PARAMETER_GROUP_NAME).addLabel("Name ..."));
			formWidget.addFormInputWidget(new FormInputSubmitWidget("create"));
			widgets.add(formWidget);
			return widgets;
		}
		catch (final GalleryServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return new ExceptionWidget(e);
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return new ExceptionWidget(e);
		}
	}

	private GalleryGroupIdentifier createGroup(final SessionIdentifier sessionIdentifier, final String name) throws GalleryServiceException, LoginRequiredException,
			PermissionDeniedException, ValidationException {
		final GalleryGroupIdentifier galleryGroupIdentifier = galleryService.createGroup(sessionIdentifier, name);
		return galleryGroupIdentifier;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}
}
