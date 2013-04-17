package de.benjaminborbe.util.gui.servlet;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.password.PasswordCharacter;
import de.benjaminborbe.tools.password.PasswordGenerator;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.util.gui.UtilGuiConstants;
import de.benjaminborbe.website.form.FormCheckboxWidget;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class UtilGuiPasswordGeneratorServlet extends WebsiteHtmlServlet {

	private static final int DEFAULT_PASSWORD_AMOUNT = 10;

	private static final long serialVersionUID = 2429004714466731564L;

	private static final int DEFAULT_LENGHT = 8;

	private static final String TITLE = "PasswordGenerator";

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
		final UrlUtil urlUtil,
		final AuthorizationService authorizationService,
		final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
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

		final List<PasswordCharacter> passwordCharacters = new ArrayList<>();
		if ("true".equals(request.getParameter(UtilGuiConstants.PARAMETER_PASSWORD_UPPER))) {
			passwordCharacters.add(PasswordCharacter.UPPER);
		}
		if ("true".equals(request.getParameter(UtilGuiConstants.PARAMETER_PASSWORD_LOWER))) {
			passwordCharacters.add(PasswordCharacter.LOWER);
		}
		if ("true".equals(request.getParameter(UtilGuiConstants.PARAMETER_PASSWORD_NUMBER))) {
			passwordCharacters.add(PasswordCharacter.NUMBER);
		}
		if ("true".equals(request.getParameter(UtilGuiConstants.PARAMETER_PASSWORD_SPECIAL))) {
			passwordCharacters.add(PasswordCharacter.SPECIAL);
		}

		if (!passwordCharacters.isEmpty()) {
			final int amount = parseUtil.parseInt(request.getParameter(UtilGuiConstants.PARAMETER_PASSWORD_AMOUNT), DEFAULT_PASSWORD_AMOUNT);
			for (int i = 0; i < amount; ++i) {
				final int length = parseUtil.parseInt(request.getParameter(UtilGuiConstants.PARAMETER_PASSWORD_LENGTH), DEFAULT_LENGHT);
				ul.add(utilPasswordGenerator.generatePassword(length, passwordCharacters));
			}
			widgets.add(ul);
		}

		widgets.add(new H2Widget("Generator"));

		final FormWidget form = new FormWidget();
		form.addFormInputWidget(new FormInputTextWidget(UtilGuiConstants.PARAMETER_PASSWORD_LENGTH).addLabel("Length:").addDefaultValue(DEFAULT_LENGHT));
		form.addFormInputWidget(new FormInputTextWidget(UtilGuiConstants.PARAMETER_PASSWORD_AMOUNT).addLabel("Amount:").addDefaultValue(DEFAULT_PASSWORD_AMOUNT));

		form.addFormInputWidget(new FormCheckboxWidget(UtilGuiConstants.PARAMETER_PASSWORD_UPPER).addLabel("Upper").setCheckedDefault(false));
		form.addFormInputWidget(new FormCheckboxWidget(UtilGuiConstants.PARAMETER_PASSWORD_LOWER).addLabel("Lower").setCheckedDefault(false));
		form.addFormInputWidget(new FormCheckboxWidget(UtilGuiConstants.PARAMETER_PASSWORD_NUMBER).addLabel("Number").setCheckedDefault(false));
		form.addFormInputWidget(new FormCheckboxWidget(UtilGuiConstants.PARAMETER_PASSWORD_SPECIAL).addLabel("Special").setCheckedDefault(false));

		form.addFormInputWidget(new FormInputSubmitWidget("generate"));
		widgets.add(form);

		return widgets;
	}

}
