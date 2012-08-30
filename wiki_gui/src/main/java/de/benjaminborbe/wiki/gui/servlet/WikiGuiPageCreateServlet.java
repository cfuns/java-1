package de.benjaminborbe.wiki.gui.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
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
import de.benjaminborbe.website.form.FormInputTextareaWidget;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;
import de.benjaminborbe.wiki.api.WikiPageCreateException;
import de.benjaminborbe.wiki.api.WikiPageIdentifier;
import de.benjaminborbe.wiki.api.WikiService;
import de.benjaminborbe.wiki.api.WikiServiceException;
import de.benjaminborbe.wiki.api.WikiSpaceIdentifier;
import de.benjaminborbe.wiki.gui.WikiGuiConstants;

@Singleton
public class WikiGuiPageCreateServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Wiki - Create Page";

	private final WikiService wikiService;

	@Inject
	public WikiGuiPageCreateServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final AuthenticationService authenticationService,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider,
			final RedirectUtil redirectUtil,
			final UrlUtil urlUtil,
			final WikiService wikiService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, httpContextProvider, redirectUtil, urlUtil);
		this.wikiService = wikiService;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException {
		try {

			logger.debug("render " + getClass().getSimpleName());
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final String spaceId = request.getParameter(WikiGuiConstants.PARAMETER_SPACE_ID);
			final String pageTitle = request.getParameter(WikiGuiConstants.PARAMETER_PAGE_TITLE);
			final String pageContent = request.getParameter(WikiGuiConstants.PARAMETER_PAGE_CONTENT);

			if (spaceId != null && pageTitle != null && pageContent != null) {
				try {
					final WikiSpaceIdentifier wikiSpaceIdentifier = wikiService.getSpaceByName(spaceId);
					final WikiPageIdentifier wikiPageIdentifier = wikiService.createPage(wikiSpaceIdentifier, pageTitle, pageContent);
					throw new RedirectException(request.getContextPath() + "/wiki/page/show?" + WikiGuiConstants.PARAMETER_PAGE_ID + "=" + wikiPageIdentifier + "&"
							+ WikiGuiConstants.PARAMETER_SPACE_ID + "=" + wikiSpaceIdentifier);
				}
				catch (final WikiPageCreateException e) {
					widgets.add("add page failed!");
					final UlWidget ul = new UlWidget();
					for (final ValidationError validationError : e.getErrors()) {
						ul.add(validationError.getMessage());
					}
					widgets.add(ul);
				}
			}

			final FormWidget form = new FormWidget("");
			form.addFormInputWidget(new FormInputHiddenWidget(WikiGuiConstants.PARAMETER_SPACE_ID));
			form.addFormInputWidget(new FormInputTextWidget(WikiGuiConstants.PARAMETER_PAGE_TITLE).addPlaceholder("Title ..."));
			form.addFormInputWidget(new FormInputTextareaWidget(WikiGuiConstants.PARAMETER_PAGE_CONTENT).addPlaceholder("Content ..."));
			form.addFormInputWidget(new FormInputSubmitWidget("create"));
			widgets.add(form);

			return widgets;
		}
		catch (final WikiServiceException e) {
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}
}
