package de.benjaminborbe.portfolio.gui.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.portfolio.gui.PortfolioGuiConstants;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.http.HttpFilter;

@Singleton
public class PortfolioCacheFilter extends HttpFilter {

	private final CalendarUtil calendarUtil;

	@Inject
	public PortfolioCacheFilter(final Logger logger, final CalendarUtil calendarUtil) {
		super(logger);
		this.calendarUtil = calendarUtil;
	}

	private boolean isCacheable(final String uri) {
		if (uri.indexOf(PortfolioGuiConstants.URL_IMAGE) != -1) {
			return true;
		}
		if (uri.indexOf(PortfolioGuiConstants.URL_GALLERY) != -1) {
			return true;
		}
		if (uri.indexOf(PortfolioGuiConstants.URL_LINKS) != -1) {
			return true;
		}
		if (uri.indexOf(PortfolioGuiConstants.URL_CONTACT) != -1) {
			return true;
		}
		if (uri.indexOf(".css") != -1) {
			return true;
		}
		if (uri.indexOf("js") != -1) {
			return true;
		}
		if (uri.indexOf(".gif") != -1) {
			return true;
		}
		if (uri.indexOf(".jpg") != -1) {
			return true;
		}
		if (uri.indexOf(".png") != -1) {
			return true;
		}
		return false;
	}

	@Override
	public void doFilter(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws IOException, ServletException {
		try {
			final String uri = request.getRequestURI();
			if (isCacheable(uri)) {
				logger.debug("cache: " + uri);
				final int days = 7;
				// seconds
				final int cacheAge = days * 24 * 60 * 60;
				final long expiry = calendarUtil.getTime() + cacheAge * 1000;
				response.setDateHeader("Expires", expiry);
				response.setHeader("Cache-Control", "max-age=" + cacheAge);
			}
			else {
				logger.debug("not cache: " + uri);
			}
		}
		finally {
			filterChain.doFilter(request, response);
		}
	}

}
