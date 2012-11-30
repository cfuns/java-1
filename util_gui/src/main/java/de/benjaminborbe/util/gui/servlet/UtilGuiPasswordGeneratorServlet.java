package de.benjaminborbe.util.gui.servlet;

import java.io.IOException;

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
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.password.PasswordCharacter;
import de.benjaminborbe.tools.password.PasswordGenerator;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;

@Singleton
public class UtilGuiPasswordGeneratorServlet extends WebsiteHtmlServlet {

	private static final int PASSWORD_AMOUNT = 10;

	private static final long serialVersionUID = 2429004714466731564L;

	private static final PasswordCharacter[] DEFAULT_CHARACTERS = { PasswordCharacter.LOWER, PasswordCharacter.UPPER, PasswordCharacter.NUMBER,
			PasswordCharacter.SPECIAL };

	private static final int DEFAULT_LENGHT = 8;

	private static final String TITLE = "PasswordGenerator";

	private static final String PARAMETER_LENGTH = "length";

	private final PasswordGenerator utilPasswordGenerator;

	private final ParseUtil parseUtil;

	@Inject
	public UtilGuiPasswordGeneratorServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final AuthenticationService authenticationService,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider,
			final PasswordGenerator utilPasswordGenerator,
			final RedirectUtil redirectUtil,
			final UrlUtil urlUtil,
			final AuthorizationService authorizationService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.utilPasswordGenerator = utilPasswordGenerator;
		this.parseUtil = parseUtil;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final ListWidget widgets = new ListWidget();
		widgets.add(new H1Widget(getTitle()));
		final UlWidget ul = new UlWidget();
		for (int i = 0; i < PASSWORD_AMOUNT; ++i) {
			final int length = parseUtil.parseInt(request.getParameter(PARAMETER_LENGTH), DEFAULT_LENGHT);
			ul.add(utilPasswordGenerator.generatePassword(length, DEFAULT_CHARACTERS));
		}
		widgets.add(ul);
		return widgets;
	}

}
