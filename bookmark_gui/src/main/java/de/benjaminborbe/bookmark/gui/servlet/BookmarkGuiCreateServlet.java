package de.benjaminborbe.bookmark.gui.servlet;

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
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormMethod;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;

@Singleton
public class BookmarkGuiCreateServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 4468520728605522219L;

	private static final String TITLE = "BookmarkGui - Create";

	@Inject
	public BookmarkGuiCreateServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final AuthenticationService authenticationService,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, httpContextProvider);
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException {
		logger.trace("printContent");
		final ListWidget widgets = new ListWidget();
		widgets.add(new H1Widget(getTitle()));
		final String action = request.getContextPath() + "/save";
		final FormWidget formWidget = new FormWidget(action).addMethod(FormMethod.GET);
		formWidget.addFormInputWidget(new FormInputTextWidget("url").addLabel("Url").addPlaceholder("url ..."));
		formWidget.addFormInputWidget(new FormInputTextWidget("name").addLabel("Name").addPlaceholder("name ..."));
		formWidget.addFormInputWidget(new FormInputTextWidget("description").addLabel("Description").addPlaceholder("description ..."));
		formWidget.addFormInputWidget(new FormInputTextWidget("keywords").addLabel("Keywords").addPlaceholder("keywords ..."));
		formWidget.addFormInputWidget(new FormInputSubmitWidget("create"));
		widgets.add(formWidget);
		return widgets;
	}
}
