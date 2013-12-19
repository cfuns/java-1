package de.benjaminborbe.util.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormInputTextareaWidget;
import de.benjaminborbe.website.form.FormMethod;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class UtilGuiRemoveTagServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 6210254616531116633L;

	private static final String TITLE = "Util - Remove Tag";

	private static final String PARAMETER_CONTENT = "content";

	private static final String PARAMETER_TAG = "tag";

	private final Logger logger;

	@Inject
	public UtilGuiRemoveTagServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final NavigationWidget navigationWidget,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final CacheService cacheService
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
	}

	protected static String removeTag(final String content, final String tag) {
		if (content == null) {
			return content;
		}
		final String pattern = "</?" + tag + "(|\\s[^>]+)>";
		return content.replaceAll(pattern, " ").replaceAll("\n\\s+\n", "\n");
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
		PermissionDeniedException, RedirectException, LoginRequiredException {
		logger.trace("printContent");
		final ListWidget widgets = new ListWidget();
		widgets.add(new H1Widget(getTitle()));

		final String content = request.getParameter(PARAMETER_CONTENT);
		final String tag = request.getParameter(PARAMETER_TAG);

		final FormWidget form = new FormWidget().addMethod(FormMethod.POST);
		form.addFormInputWidget(new FormInputTextWidget(PARAMETER_TAG).addLabel("Tag:").addDefaultValue("span"));
		final FormInputTextareaWidget formInputWidget = new FormInputTextareaWidget(PARAMETER_CONTENT).addLabel("HTML:");
		if (content != null) {
			formInputWidget.addValue(removeTag(content, tag));
		}
		form.addFormInputWidget(formInputWidget);
		form.addFormInputWidget(new FormInputSubmitWidget("remove"));
		widgets.add(form);

		return widgets;
	}
}
