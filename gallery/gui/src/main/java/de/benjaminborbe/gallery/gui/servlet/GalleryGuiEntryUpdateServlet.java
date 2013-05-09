package de.benjaminborbe.gallery.gui.servlet;

import com.google.inject.Provider;
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
import de.benjaminborbe.gallery.api.GalleryCollectionIdentifier;
import de.benjaminborbe.gallery.api.GalleryEntry;
import de.benjaminborbe.gallery.api.GalleryEntryIdentifier;
import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.gallery.api.GalleryServiceException;
import de.benjaminborbe.gallery.gui.GalleryGuiConstants;
import de.benjaminborbe.gallery.gui.util.GalleryGuiLinkFactory;
import de.benjaminborbe.gallery.gui.util.GalleryGuiWebsiteHtmlServlet;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.validation.ValidationResultImpl;
import de.benjaminborbe.website.form.FormInputHiddenWidget;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormMethod;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.ValidationExceptionWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class GalleryGuiEntryUpdateServlet extends GalleryGuiWebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Gallery - Entry - Update";

	private final GalleryService galleryService;

	private final Logger logger;

	private final AuthenticationService authenticationService;

	private final ParseUtil parseUtil;

	private final GalleryGuiLinkFactory galleryGuiLinkFactory;

	@Inject
	public GalleryGuiEntryUpdateServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final AuthenticationService authenticationService,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final GalleryService galleryService,
		final AuthorizationService authorizationService,
		final GalleryGuiLinkFactory galleryGuiLinkFactory,
		final CacheService cacheService
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService, galleryService);
		this.galleryService = galleryService;
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.parseUtil = parseUtil;
		this.galleryGuiLinkFactory = galleryGuiLinkFactory;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createGalleryContentWidget(final HttpServletRequest request) throws IOException,
		PermissionDeniedException, RedirectException, LoginRequiredException {

		try {
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));
			final String id = request.getParameter(GalleryGuiConstants.PARAMETER_ENTRY_ID);
			final String collectionId = request.getParameter(GalleryGuiConstants.PARAMETER_COLLECTION_ID);
			final String referer = request.getParameter(GalleryGuiConstants.PARAMETER_REFERER);
			final String name = request.getParameter(GalleryGuiConstants.PARAMETER_ENTRY_NAME);
			final String prio = request.getParameter(GalleryGuiConstants.PARAMETER_ENTRY_PRIO);
			final String shared = request.getParameter(GalleryGuiConstants.PARAMETER_ENTRY_SHARED);

			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final GalleryEntryIdentifier galleryEntryIdentifier = galleryService.createEntryIdentifier(id);
			final GalleryEntry galleryEntry = galleryService.getEntry(sessionIdentifier, galleryEntryIdentifier);

			if (collectionId != null && prio != null && name != null && shared != null) {
				try {

					final GalleryCollectionIdentifier galleryCollectionIdentifier = galleryService.createCollectionIdentifier(collectionId);
					updateEntry(sessionIdentifier, galleryEntryIdentifier, galleryCollectionIdentifier, name, prio, shared);

					if (referer != null) {
						throw new RedirectException(referer);
					} else {
						throw new RedirectException(galleryGuiLinkFactory.entryListUrl(request, galleryCollectionIdentifier));
					}
				} catch (final ValidationException e) {
					widgets.add("update collection => failed");
					widgets.add(new ValidationExceptionWidget(e));
				}
			}
			final FormWidget form = new FormWidget().addMethod(FormMethod.POST);
			form.addFormInputWidget(new FormInputHiddenWidget(GalleryGuiConstants.PARAMETER_ENTRY_ID));
			form.addFormInputWidget(new FormInputHiddenWidget(GalleryGuiConstants.PARAMETER_COLLECTION_ID).addValue(galleryEntry.getCollectionId()));
			form.addFormInputWidget(new FormInputTextWidget(GalleryGuiConstants.PARAMETER_ENTRY_NAME).addLabel("Name").addPlaceholder("name...").addDefaultValue(galleryEntry.getName()));
			form.addFormInputWidget(new FormInputTextWidget(GalleryGuiConstants.PARAMETER_ENTRY_PRIO).addLabel("Prio").addPlaceholder("prio...")
				.addDefaultValue(galleryEntry.getPriority()));
			form.addFormInputWidget(new FormInputTextWidget(GalleryGuiConstants.PARAMETER_ENTRY_SHARED).addLabel("Shared").addPlaceholder("shared...")
				.addDefaultValue(galleryEntry.getShared()));
			form.addFormInputWidget(new FormInputSubmitWidget("update"));
			widgets.add(form);

			final ListWidget links = new ListWidget();
			links.add(galleryGuiLinkFactory.listEntries(request, galleryEntry.getCollectionId()));
			links.add(" ");
			widgets.add(links);

			return widgets;
		} catch (final GalleryServiceException e) {
			logger.warn(e.getClass().getSimpleName(), e);
			return new ExceptionWidget(e);
		} catch (final AuthenticationServiceException e) {
			logger.warn(e.getClass().getSimpleName(), e);
			return new ExceptionWidget(e);
		}
	}

	private void updateEntry(
		final SessionIdentifier sessionIdentifier, final GalleryEntryIdentifier galleryEntryIdentifier,
		final GalleryCollectionIdentifier galleryCollectionIdentifier, final String entryName, final String prioString, final String sharedString
	) throws ValidationException,
		GalleryServiceException, LoginRequiredException, PermissionDeniedException {

		Long prio;
		Boolean shared;
		final List<ValidationError> errors = new ArrayList<>();
		{
			try {
				if (prioString == null || prioString.length() == 0) {
					prio = null;
				} else {
					prio = parseUtil.parseLong(prioString);
				}
			} catch (final ParseException e) {
				prio = null;
				errors.add(new ValidationErrorSimple("illegal prio"));
			}
		}
		{
			try {
				shared = parseUtil.parseBoolean(sharedString);
			} catch (final ParseException e) {
				shared = null;
				errors.add(new ValidationErrorSimple("illegal shared"));
			}
		}

		if (!errors.isEmpty()) {
			throw new ValidationException(new ValidationResultImpl(errors));
		} else {
			logger.info("updateEntry(" + sessionIdentifier + ", " + galleryEntryIdentifier + ", " + galleryCollectionIdentifier + ", " + entryName + ", " + prio + ", " + shared + ")");
			galleryService.updateEntry(sessionIdentifier, galleryEntryIdentifier, galleryCollectionIdentifier, entryName, prio, shared);
		}
	}
}
