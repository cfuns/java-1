package de.benjaminborbe.search.gui.servlet;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.search.gui.SearchGuiConstants;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.servlet.WebsiteWidgetServlet;
import de.benjaminborbe.website.util.HtmlContentWidget;

@Singleton
public class SearchGuiOsdServlet extends WebsiteWidgetServlet {

	private static final long serialVersionUID = 2841304297821086170L;

	private final Logger logger;

	private final UrlUtil urlUtil;

	@Inject
	public SearchGuiOsdServlet(
			final Logger logger,
			final UrlUtil urlUtil,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final Provider<HttpContext> httpContextProvider,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService) {
		super(logger, urlUtil, calendarUtil, timeZoneUtil, httpContextProvider, authenticationService, authorizationService);
		this.logger = logger;
		this.urlUtil = urlUtil;
	}

	@Override
	public Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		logger.trace("osd.xml");
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");

		final String baseUrl = urlUtil.buildBaseUrl(request);
		final String shortName = "BB Search";
		final String description = "BB Search";
		final String longName = "BB Search";
		final String email = "bborbe@rocketnews.de";

		final StringWriter sw = new StringWriter();

		sw.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sw.append("<OpenSearchDescription xmlns=\"http://a9.com/-/spec/opensearch/1.1/\">");
		sw.append("<ShortName>" + shortName + "</ShortName>");
		sw.append("<LongName>" + longName + "</LongName>");
		sw.append("<Description>" + description + "</Description>");
		sw.append("<Contact>" + email + "</Contact>");
		sw.append("<Url type=\"application/x-suggestions+json\" method=\"get\" rel=\"suggestions\" template=\"" + baseUrl + "/" + SearchGuiConstants.NAME
				+ "/suggest?q={searchTerms}\" />");
		sw.append("<Url type=\"text/html\" method=\"get\" template=\"" + baseUrl + "/search?q={searchTerms}\" />");
		sw.append("<Image width=\"16\" height=\"16\">data:image/x-icon;base64,R0lGODlhEAAQAOeSACEMFB8OFiUMFCsOGDgKGDEQGCoVHBgdIzEUHUAOGDUWHDUWHTEYITEaIRgkOjoYIUIWH1AQGjEeJ0YWHVYQGkQYIkoWIEYYIToeJT4dKj4eJU4YIRgtRlYWIUocJ0IkLX4KFmsUH20UIWkYIWIcJXEWIYQQGD4vOjE6RoQYI0Y0OEI1Qko1PFgvOm0nL041PidEZmQvNkI+REJCSGQzPUpAQlY+RowxOnU7RHBCSpA1PnhASGFLWHlCSmROWHVJUGZRWG9OUohGTHlOVGRXXWVXX3NSWohKTmZYYGFcXGtaXmZcamNjZ4xYYHBmaHhjY3Bma31jaYxeYmhteG1tclZ0kHVtc4Bxd5tnb45vc357gW2EmJB4gJB5f4N/hH6BiYx8gGSKpG+ImJCAiJSAhKCAhpGJj6CFipCQkISWnnOcsZKSlI6UoKKNkq6LjpyUlK+MkJyUnLaVmqWeoamhpZ6ptamnrampq5qvuKGvt7GprbWosaa0vrOyt8KttaS6ytSrr721vcK2vri7w9q1t8a/wsq/xMbCxszCxs7GzsXU2tLS0tjS09jY2NTa2+Lc3N7i4uLr6////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////yH5BAEKAP8ALAAAAAAQABAAAAjKAP8JZESnjUCBWg4q9EFjhxs5/5TYeBJD4UEgQnhgiUIAx5EeNywKrJBjjKMRhB4hIiOykAUSXRCIALQFRRaRcwz8ADNgg44hIVIMiiSwjxcoGZbwKbKAQokWRtg0+meHyBkqX6oo+qfhgoc4LqbUESlwz4QOJhQk8ENW4JsCEEC84NJWoB4BARo0qSsQCQAGEaTUTeSEhQQMH8wIgmTxSpIaVpg8gLAijRg1eQSiOWEIzp0ZBxxwgBEGz5+Da2SoCFLm36JAh0QGBAA7</Image>");
		sw.append("<Query role=\"example\" searchTerms=\"Search\" />");
		sw.append("<Developer>Tester</Developer>");
		sw.append("<Attribution>Testing</Attribution>");
		sw.append("<SyndicationRight>open</SyndicationRight>");
		sw.append("<AdultContent>false</AdultContent>");
		sw.append("<Language>en-us</Language>");
		sw.append("<OutputEncoding>UTF-8</OutputEncoding>");
		sw.append("<InputEncoding>UTF-8</InputEncoding>");
		sw.append("</OpenSearchDescription>");

		return new HtmlContentWidget(sw.toString());
	}

	@Override
	public boolean isLoginRequired() {
		return true;
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}
}
