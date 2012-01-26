package de.benjaminborbe.bookmark.gui.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.html.api.CssResourceRenderer;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResourceRenderer;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.FormInputSubmitWidget;
import de.benjaminborbe.website.util.FormInputTextWidget;
import de.benjaminborbe.website.util.FormWidget;

@Singleton
public class BookmarkGuiCreateServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 4468520728605522219L;

	private static final String TITLE = "BookmarkGui - Create";

	@Inject
	public BookmarkGuiCreateServlet(
			final Logger logger,
			final CssResourceRenderer cssResourceRenderer,
			final JavascriptResourceRenderer javascriptResourceRenderer,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider) {
		super(logger, cssResourceRenderer, javascriptResourceRenderer, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, httpContextProvider);
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected void printContent(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final PrintWriter out = response.getWriter();
		logger.debug("printContent");
		out.println("<h1>" + getTitle() + "</h1>");
		final String action = request.getContextPath() + "/save";
		final FormWidget formWidget = new FormWidget(action).addMethod("GET");
		formWidget.addFormInputWidget(new FormInputTextWidget("url").addLabel("Url").addPlaceholder("url ..."));
		formWidget.addFormInputWidget(new FormInputTextWidget("name").addLabel("Name").addPlaceholder("name ..."));
		formWidget.addFormInputWidget(new FormInputTextWidget("description").addLabel("Description").addPlaceholder("description ..."));
		formWidget.addFormInputWidget(new FormInputTextWidget("keywords").addLabel("Keywords").addPlaceholder("keywords ..."));
		formWidget.addFormInputWidget(new FormInputSubmitWidget("create"));
		formWidget.render(request, response, context);
	}
}
