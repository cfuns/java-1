package de.benjaminborbe.monitoring.gui.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.RequireCssResource;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.monitoring.api.MonitoringCheckResult;
import de.benjaminborbe.monitoring.api.MonitoringServiceException;
import de.benjaminborbe.monitoring.gui.util.MonitoringGuiCheckResultRenderer;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ComparatorBase;
import de.benjaminborbe.website.util.CssResourceImpl;
import de.benjaminborbe.website.util.DivWidget;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;

@Singleton
public abstract class MonitoringGuiWidgetBase implements MonitoringWidget, RequireCssResource {

	private final class CheckResultComparator extends ComparatorBase<MonitoringCheckResult, String> {

		@Override
		public String getValue(final MonitoringCheckResult o) {
			return o.toString();
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

	protected Widget getCheckWithRootNode(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			AuthenticationServiceException, MonitoringServiceException, PermissionDeniedException, LoginRequiredException {
		final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
		final List<MonitoringCheckResult> checkResults = new ArrayList<MonitoringCheckResult>(getResults(sessionIdentifier));
		Collections.sort(checkResults, new CheckResultComparator());
		final UlWidget ul = new UlWidget();
		for (final MonitoringCheckResult checkResult : checkResults) {
			logger.trace(checkResult.toString());
			ul.add(new MonitoringGuiCheckResultRenderer(checkResult, urlUtil));
		}
		return ul;
	}

	protected abstract Collection<MonitoringCheckResult> getResults(final SessionIdentifier sessionIdentifier) throws MonitoringServiceException, PermissionDeniedException,
			LoginRequiredException;

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		try {
			final ListWidget widgets = new ListWidget();
			logger.trace("render");
			widgets.add("monitoring checks started");
			widgets.add(getCheckWithRootNode(request, response, context));
			widgets.add("monitoring checks finished");
			final DivWidget div = new DivWidget(widgets);
			div.addAttribute("class", "monitoring");
			div.render(request, response, context);
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget exceptionWidget = new ExceptionWidget(e);
			exceptionWidget.render(request, response, context);
		}
		catch (final MonitoringServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget exceptionWidget = new ExceptionWidget(e);
			exceptionWidget.render(request, response, context);
		}
		catch (final PermissionDeniedException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget exceptionWidget = new ExceptionWidget(e);
			exceptionWidget.render(request, response, context);
		}
		catch (final LoginRequiredException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget exceptionWidget = new ExceptionWidget(e);
			exceptionWidget.render(request, response, context);
		}
	}

	@Override
	public List<CssResource> getCssResource(final HttpServletRequest request, final HttpServletResponse response) {
		final String contextPath = request.getContextPath();
		final List<CssResource> result = new ArrayList<CssResource>();
		result.add(new CssResourceImpl(contextPath + "/monitoring/css/style.css"));
		return result;
	}

}
