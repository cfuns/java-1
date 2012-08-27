package de.benjaminborbe.wiki.gui.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.wiki.api.WikiService;
import de.benjaminborbe.wiki.api.WikiServiceException;
import de.benjaminborbe.wiki.api.WikiSpaceIdentifier;

@Singleton
public class WikiGuiSpaceCreateServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Wiki - Create Space";

	private final WikiService wikiService;

	@Inject
	public WikiGuiSpaceCreateServlet(
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
		logger.trace("printContent");
		final ListWidget widgets = new ListWidget();
		widgets.add(new H1Widget(getTitle()));

		final String id = request.getParameter(WikiGuiParameter.PARAMETER_SPACE_ID);
		final String title = request.getParameter(WikiGuiParameter.PARAMETER_SPACE_TITLE);

		if (id != null && title != null) {
			try {
				final WikiSpaceIdentifier wikiSpaceIdentifier = wikiService.createSpace(id, title);
				if (wikiSpaceIdentifier != null) {
					throw new RedirectException(request.getContextPath() + "/wiki/page/list?space=" + wikiSpaceIdentifier);
				}
			}
			catch (final WikiServiceException e) {
				widgets.add("add space failed!");
			}
		}

		final FormWidget form = new FormWidget("");
		form.addFormInputWidget(new FormInputTextWidget(WikiGuiParameter.PARAMETER_SPACE_ID).addLabel("Code:").addPlaceholder("Code ..."));
		form.addFormInputWidget(new FormInputTextWidget(WikiGuiParameter.PARAMETER_SPACE_TITLE).addLabel("Title:").addPlaceholder("Title ..."));
		form.addFormInputWidget(new FormInputSubmitWidget("create"));
		widgets.add(form);

		return widgets;
	}
}