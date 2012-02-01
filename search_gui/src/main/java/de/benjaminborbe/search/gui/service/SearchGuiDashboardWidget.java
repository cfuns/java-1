package de.benjaminborbe.search.gui.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.RequireCssResource;
import de.benjaminborbe.html.api.RequireJavascriptResource;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.util.CssResourceImpl;
import de.benjaminborbe.website.util.JavascriptResourceImpl;

@Singleton
public class SearchGuiDashboardWidget implements DashboardContentWidget, RequireCssResource, RequireJavascriptResource {

	private static final String TITLE = "SearchDashboardWidget";

	private final static String PARAMETER_SEARCH = "q";

	private final Logger logger;

	@Inject
	public SearchGuiDashboardWidget(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		logger.debug("render");
		final PrintWriter out = response.getWriter();
		final String contextPath = request.getContextPath();
		final String searchSuggestUrl = contextPath + "/search/suggest";
		final String action = contextPath + "/search";
		final FormWidget formWidget = new FormWidget(action).addMethod("POST");
		formWidget.addFormInputWidget(new FormInputTextWidget(PARAMETER_SEARCH).addPlaceholder("searchtext ...").addId("searchBox"));
		formWidget.addFormInputWidget(new FormInputSubmitWidget("search"));
		formWidget.render(request, response, context);

		out.println("<script language=\"javascript\">");
		out.println("$(document).ready(function() {");
		out.println("$('input#searchBox').autocomplete({");
		out.println("source: '" + searchSuggestUrl + "',");
		out.println("method: 'POST',");
		out.println("minLength: 1,");
		out.println("});");
		out.println("});");
		out.println("</script>");
	}

	@Override
	public String getTitle() {
		return TITLE;
	}

	@Override
	public List<JavascriptResource> getJavascriptResource(final HttpServletRequest request, final HttpServletResponse response) {
		final List<JavascriptResource> result = new ArrayList<JavascriptResource>();
		result.add(new JavascriptResourceImpl("http://ajax.googleapis.com/ajax/libs/jquery/1.5/jquery.min.js"));
		result.add(new JavascriptResourceImpl("http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js"));
		return result;
	}

	@Override
	public List<CssResource> getCssResource(final HttpServletRequest request, final HttpServletResponse response) {
		final String contextPath = request.getContextPath();
		final List<CssResource> result = new ArrayList<CssResource>();
		result.add(new CssResourceImpl("http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css"));
		result.add(new CssResourceImpl(contextPath + "/search/css/style.css"));
		return result;
	}

	@Override
	public long getPriority() {
		return 999;
	}
}
