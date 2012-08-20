package de.benjaminborbe.monitoring.gui.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.RequireCssResource;
import de.benjaminborbe.monitoring.api.CheckResult;
import de.benjaminborbe.monitoring.api.MonitoringServiceException;
import de.benjaminborbe.monitoring.api.MonitoringWidget;
import de.benjaminborbe.monitoring.gui.util.MonitoringGuiCheckResultRenderer;
import de.benjaminborbe.tools.io.FlushPrintWriter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.util.CssResourceImpl;
import de.benjaminborbe.website.util.ExceptionWidget;

@Singleton
public abstract class MonitoringGuiWidgetBase implements MonitoringWidget, RequireCssResource {

	private final class CheckResultComparator implements Comparator<CheckResult> {

		@Override
		public int compare(final CheckResult a, final CheckResult b) {
			return a.toString().compareTo(b.toString());
		}
	}

	private final Logger logger;

	private final UrlUtil urlUtil;

	private final AuthenticationService authenticationService;

	@Inject
	public MonitoringGuiWidgetBase(final Logger logger, final UrlUtil urlUtil, final AuthenticationService authenticationService) {
		this.logger = logger;
		this.urlUtil = urlUtil;
		this.authenticationService = authenticationService;
	}

	protected void printCheckWithRootNode(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final List<CheckResult> checkResults = new ArrayList<CheckResult>(getResults(sessionIdentifier));
			Collections.sort(checkResults, new CheckResultComparator());
			final FlushPrintWriter out = new FlushPrintWriter(response.getWriter());
			out.println("<ul>");
			for (final CheckResult checkResult : checkResults) {
				logger.trace(checkResult.toString());
				out.println("<li>");
				final MonitoringGuiCheckResultRenderer renderer = new MonitoringGuiCheckResultRenderer(checkResult, urlUtil);
				renderer.render(request, response, context);
				out.println("</li>");
			}
			out.println("</ul>");

		}
		catch (final AuthenticationServiceException e) {
			final ExceptionWidget exceptionWidget = new ExceptionWidget(e);
			exceptionWidget.render(request, response, context);
		}
		catch (final MonitoringServiceException e) {
			final ExceptionWidget exceptionWidget = new ExceptionWidget(e);
			exceptionWidget.render(request, response, context);
		}
	}

	protected abstract Collection<CheckResult> getResults(final SessionIdentifier sessionIdentifier) throws MonitoringServiceException, PermissionDeniedException;

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException, PermissionDeniedException {
		logger.trace("render");
		final FlushPrintWriter out = new FlushPrintWriter(response.getWriter());
		out.println("monitoring checks started");
		printCheckWithRootNode(request, response, context);
		out.println("monitoring checks finished");
	}

	@Override
	public List<CssResource> getCssResource(final HttpServletRequest request, final HttpServletResponse response) {
		final String contextPath = request.getContextPath();
		final List<CssResource> result = new ArrayList<CssResource>();
		result.add(new CssResourceImpl(contextPath + "/monitoring/css/style.css"));
		return result;
	}

}