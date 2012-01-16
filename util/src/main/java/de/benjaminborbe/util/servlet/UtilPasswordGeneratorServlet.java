package de.benjaminborbe.util.servlet;

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
import de.benjaminborbe.util.UtilPasswordCharacter;
import de.benjaminborbe.util.UtilPasswordGenerator;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;

@Singleton
public class UtilPasswordGeneratorServlet extends WebsiteHtmlServlet {

	private static final int PASSWORD_AMOUNT = 10;

	private static final long serialVersionUID = 2429004714466731564L;

	private static final UtilPasswordCharacter[] DEFAULT_CHARACTERS = { UtilPasswordCharacter.LOWER, UtilPasswordCharacter.UPPER, UtilPasswordCharacter.NUMBER, UtilPasswordCharacter.SPECIAL };

	private static final int DEFAULT_LENGHT = 8;

	private static final String TITLE = "PasswordGenerator";

	private static final String PARAMETER_LENGTH = "length";

	private final UtilPasswordGenerator utilPasswordGenerator;

	@Inject
	public UtilPasswordGeneratorServlet(
			final Logger logger,
			final CssResourceRenderer cssResourceRenderer,
			final JavascriptResourceRenderer javascriptResourceRenderer,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider,
			final UtilPasswordGenerator utilPasswordGenerator) {
		super(logger, cssResourceRenderer, javascriptResourceRenderer, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, httpContextProvider);
		this.utilPasswordGenerator = utilPasswordGenerator;
	}

	protected void printContent(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		final PrintWriter out = response.getWriter();
		out.println("<h1>PasswordGenerator</h1>");
		out.println("<ul>");
		for (int i = 0; i < PASSWORD_AMOUNT; ++i) {
			out.println("<li>");
			final int length = parseUtil.parseInt(request.getParameter(PARAMETER_LENGTH), DEFAULT_LENGHT);
			out.println(utilPasswordGenerator.generatePassword(length, DEFAULT_CHARACTERS));
			out.println("</li>");
		}
		out.println("</ul>");
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

}
