package de.benjaminborbe.performance.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.performance.util.PerformanceTracker;
import de.benjaminborbe.tools.http.HttpFilter;
import de.benjaminborbe.tools.util.Duration;
import de.benjaminborbe.tools.util.DurationUtil;

@Singleton
public class PerformanceFilter extends HttpFilter {

	private final DurationUtil durationUtil;

	private final PerformanceTracker performanceTracker;

	@Inject
	public PerformanceFilter(final Logger logger, final DurationUtil durationUtil, final PerformanceTracker performanceTracker) {
		super(logger);
		this.durationUtil = durationUtil;
		this.performanceTracker = performanceTracker;
	}

	@Override
	public void doFilter(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse, final FilterChain filterChain) throws IOException, ServletException {
		final Duration duration = durationUtil.getDuration();
		final String uri = httpServletRequest.getRequestURI();
		try {
			filterChain.doFilter(httpServletRequest, httpServletResponse);
		}
		finally {
			final long time = duration.getTime();
			performanceTracker.track(uri, time);
		}
	}

}
