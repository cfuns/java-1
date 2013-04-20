package de.benjaminborbe.search.gui.service;

import javax.inject.Inject;
import javax.inject.Singleton;
import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.RequireCssResource;
import de.benjaminborbe.html.api.RequireJavascriptResource;
import de.benjaminborbe.search.gui.SearchGuiConstants;
import de.benjaminborbe.search.gui.util.SearchGuiLinkFactory;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormMethod;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.util.CssResourceImpl;
import de.benjaminborbe.website.util.JavascriptResourceImpl;
import de.benjaminborbe.website.util.JavascriptWidget;
import de.benjaminborbe.website.util.ListWidget;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class SearchGuiDashboardWidget implements DashboardContentWidget, RequireCssResource, RequireJavascriptResource {

	private static final String TITLE = "SearchDashboardWidget";

	private final Logger logger;

	private final SearchGuiLinkFactory searchGuiLinkFactory;

	@Inject
	public SearchGuiDashboardWidget(final Logger logger, final SearchGuiLinkFactory searchGuiLinkFactory) {
		this.logger = logger;
		this.searchGuiLinkFactory = searchGuiLinkFactory;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		logger.trace("render");
		final ListWidget widgets = new ListWidget();
		final FormWidget formWidget = new FormWidget(searchGuiLinkFactory.searchUrl(request)).addMethod(FormMethod.POST);
		formWidget.addFormInputWidget(new FormInputTextWidget(SearchGuiConstants.PARAMETER_SEARCH).addPlaceholder("searchtext...").addId("searchBox").setBr(false));
		formWidget.addFormInputWidget(new FormInputSubmitWidget("search"));
		widgets.add(formWidget);

		if (request.getParameter(SearchGuiConstants.PARAMETER_SEARCH_AUTOCOMPLETE) != null) {
			final StringWriter sw = new StringWriter();
			sw.append("$(document).ready(function() {");
			sw.append("$('input#searchBox').autocomplete({");
			sw.append("source: '" + searchGuiLinkFactory.suggestUrl(request) + "',");
			sw.append("method: 'POST',");
			sw.append("minLength: 1,");
			sw.append("});");
			sw.append("});");
			widgets.add(new JavascriptWidget(sw.toString()));
		}

		widgets.render(request, response, context);
	}

	@Override
	public String getTitle() {
		return TITLE;
	}

	@Override
	public List<JavascriptResource> getJavascriptResource(final HttpServletRequest request, final HttpServletResponse response) {
		final List<JavascriptResource> result = new ArrayList<>();
		result.add(new JavascriptResourceImpl(request.getScheme() + "://ajax.googleapis.com/ajax/libs/jquery/1.5/jquery.min.js"));
		result.add(new JavascriptResourceImpl(request.getScheme() + "://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js"));
		return result;
	}

	@Override
	public List<CssResource> getCssResource(final HttpServletRequest request, final HttpServletResponse response) {
		final String contextPath = request.getContextPath();
		final List<CssResource> result = new ArrayList<>();
		result.add(new CssResourceImpl(request.getScheme() + "://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css"));
		result.add(new CssResourceImpl(contextPath + "/search/css/style.css"));
		return result;
	}

	@Override
	public long getPriority() {
		return 999;
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}

	@Override
	public String getName() {
		return SearchGuiConstants.NAME;
	}

}
