package de.benjaminborbe.website.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.CssResourceRenderer;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.JavascriptResourceRenderer;
import de.benjaminborbe.html.api.RequireCssResource;
import de.benjaminborbe.html.api.RequireJavascriptResource;
import de.benjaminborbe.html.api.Widget;

@Singleton
public abstract class WebsiteMainServlet extends HttpServlet {

	private static final long serialVersionUID = 1544940069187374367L;

	protected final Logger logger;

	protected final CssResourceRenderer cssResourceRenderer;

	protected final JavascriptResourceRenderer javascriptResourceRenderer;

	@Inject
	public WebsiteMainServlet(final Logger logger, final CssResourceRenderer cssResourceRenderer, final JavascriptResourceRenderer javascriptResourceRenderer) {
		this.logger = logger;
		this.cssResourceRenderer = cssResourceRenderer;
		this.javascriptResourceRenderer = javascriptResourceRenderer;
	}

	protected abstract String getTitle();

	protected abstract Collection<Widget> getWidgets();

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		logger.debug("service");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		printHtml(request, response);
	}

	protected void printHtml(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		logger.debug("printHtml");
		final PrintWriter out = response.getWriter();
		out.println("<html>");
		printHead(request, response);
		printBody(request, response);
		out.println("</html>");
	}

	protected void printBody(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		final PrintWriter out = response.getWriter();
		logger.debug("printBody");
		out.println("<body>");
		out.println("</body>");
	}

	protected void printHead(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		logger.debug("printHead");
		final PrintWriter out = response.getWriter();
		out.println("<head>");
		out.println("<title>" + getTitle() + "</title>");
		javascriptResourceRenderer.render(request, response, getJavascriptResources(request, response));
		cssResourceRenderer.render(request, response, getCssResources(request, response));
		out.println("</head>");
	}

	protected Collection<JavascriptResource> getJavascriptResources(final HttpServletRequest request, final HttpServletResponse response) {
		logger.debug("getJavascriptResources");
		final Set<JavascriptResource> result = new HashSet<JavascriptResource>();
		final Collection<Widget> widgets = getWidgets();
		logger.debug("found " + widgets.size() + " widgets");
		for (final Widget widget : widgets) {
			logger.debug("try widget " + widget.getClass().getName());
			if (widget instanceof RequireJavascriptResource) {
				logger.debug("widget " + widget.getClass().getName() + " requireJavascriptResource");
				final RequireJavascriptResource requireJavascriptResource = (RequireJavascriptResource) widget;
				result.addAll(requireJavascriptResource.getJavascriptResource(request, response));
			}
		}
		return result;
	}

	protected Collection<CssResource> getCssResources(final HttpServletRequest request, final HttpServletResponse response) {
		logger.debug("getCssResources");
		final Set<CssResource> result = new HashSet<CssResource>();
		final Collection<Widget> widgets = getWidgets();
		logger.debug("found " + widgets.size() + " widgets");
		for (final Widget widget : widgets) {
			logger.debug("try widget " + widget.getClass().getName());
			if (widget instanceof RequireCssResource) {
				logger.debug("widget " + widget.getClass().getName() + " requireJavascriptResource");
				final RequireCssResource requireCssResource = (RequireCssResource) widget;
				result.addAll(requireCssResource.getCssResource(request, response));
			}
		}
		return result;
	}

}
