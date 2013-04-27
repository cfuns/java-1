package de.benjaminborbe.slash.gui.servlet;

import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsService;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Singleton
public class SlashGuiLogFilter implements Filter {

	private static final String REPORT_NAME = "RequestCounter";

	private final Logger logger;

	private final AnalyticsService analyticsService;

	private final AnalyticsReportIdentifier analyticsReportIdentifier = new AnalyticsReportIdentifier(REPORT_NAME);

	@Inject
	public SlashGuiLogFilter(final Logger logger, final AnalyticsService analyticsService) {
		this.logger = logger;
		this.analyticsService = analyticsService;
	}

	@Override
	public void doFilter(
		final ServletRequest servletRequest,
		final ServletResponse servletResponse,
		final FilterChain filterChain
	) throws IOException, ServletException {
		try {
			// log cookies
			if (servletRequest instanceof HttpServletRequest) {
				final HttpServletRequest request = (HttpServletRequest) servletRequest;
				final String requestUrl = request.getRequestURL().toString();
				logger.trace("requestUrl: " + requestUrl);
			}

			try {
				analyticsService.addReportValue(analyticsReportIdentifier);
			} catch (final Exception e) {
				logger.warn(e.getClass().getName(), e);
			}
		} finally {
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

}
