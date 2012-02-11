package de.benjaminborbe.websearch.gui.servlet;

import java.io.IOException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.html.api.CssResourceRenderer;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResourceRenderer;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.websearch.api.PageIdentifierImpl;
import de.benjaminborbe.websearch.api.WebsearchService;
import de.benjaminborbe.websearch.api.WebsearchServiceException;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;

@Singleton
public class WebsearchGuiExpirePageServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Websearch - Expire Page";

	private static final String PARAMETER_URL = "url";

	private final WebsearchService websearchService;

	@Inject
	public WebsearchGuiExpirePageServlet(
			final Logger logger,
			final CssResourceRenderer cssResourceRenderer,
			final JavascriptResourceRenderer javascriptResourceRenderer,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final AuthenticationService authenticationService,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider,
			final WebsearchService websearchService) {
		super(logger, cssResourceRenderer, javascriptResourceRenderer, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, httpContextProvider);
		this.websearchService = websearchService;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected void printContent(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final String url = request.getParameter(PARAMETER_URL);
			if (url != null) {
				websearchService.expirePage(new PageIdentifierImpl(new URL(url)));
			}
			else {
				final FormWidget form = new FormWidget("");
				form.addFormInputWidget(new FormInputTextWidget("url").addLabel("Url").addPlaceholder("Url ..."));
				form.addFormInputWidget(new FormInputSubmitWidget("expire"));
				widgets.add(form);
			}

			widgets.render(request, response, context);
		}
		catch (final WebsearchServiceException e) {
			final ExceptionWidget widget = new ExceptionWidget(e);
			widget.render(request, response, context);
		}
	}
}
