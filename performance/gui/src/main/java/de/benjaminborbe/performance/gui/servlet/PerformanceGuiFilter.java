package de.benjaminborbe.performance.gui.servlet;

import javax.inject.Inject;
import javax.inject.Singleton;
import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.performance.api.PerformanceService;
import de.benjaminborbe.tools.http.HttpFilter;
import de.benjaminborbe.tools.util.Duration;
import de.benjaminborbe.tools.util.DurationUtil;
import org.slf4j.Logger;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class PerformanceGuiFilter extends HttpFilter {

	private static final String REPORT_NAME = "RequestDuration";

	private final DurationUtil durationUtil;

	private final PerformanceService performanceService;

	private final AnalyticsService analyticsService;

	private final AnalyticsReportIdentifier analyticsReportIdentifier = new AnalyticsReportIdentifier(REPORT_NAME);

	@Inject
	public PerformanceGuiFilter(final Logger logger, final DurationUtil durationUtil, final PerformanceService performanceService, final AnalyticsService analyticsService) {
		super(logger);
		this.durationUtil = durationUtil;
		this.performanceService = performanceService;
		this.analyticsService = analyticsService;
	}

	@Override
	public void doFilter(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse, final FilterChain filterChain) throws IOException,
		ServletException {
		final Duration duration = durationUtil.getDuration();
		final String uri = httpServletRequest.getRequestURI();
		try {
			filterChain.doFilter(httpServletRequest, httpServletResponse);
		} finally {
			final long time = duration.getTime();
			performanceService.track(uri, time);
			try {
				analyticsService.addReportValue(analyticsReportIdentifier, time);
			} catch (final Exception e) {
				logger.warn(e.getClass().getName(), e);
			}
		}
	}

}
