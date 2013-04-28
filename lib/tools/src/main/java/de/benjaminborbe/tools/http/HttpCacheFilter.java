package de.benjaminborbe.tools.http;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class HttpCacheFilter extends HttpFilter {

	private final Map<String, HttpCacheEntry> cache = new HashMap<>();

	@Inject
	public HttpCacheFilter(final Logger logger) {
		super(logger);
	}

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(
		final HttpServletRequest request,
		final HttpServletResponse response,
		final FilterChain filterChain
	) throws IOException, ServletException {
		final String identifier = buildIdentifier(request);
		if (!cache.containsKey(identifier)) {
			logger.trace("cache miss for " + identifier);
			final HttpServletResponseBuffer httpServletResponseAdapter = new HttpServletResponseBuffer(response);
			filterChain.doFilter(request, httpServletResponseAdapter);
			cache.put(identifier,
				new HttpCacheEntry(httpServletResponseAdapter.getContentType(), httpServletResponseAdapter.getWriterContent(), httpServletResponseAdapter.getOutputStreamContent()));
		} else {
			logger.trace("cache hit for " + identifier);
		}
		final HttpCacheEntry cacheEntry = cache.get(identifier);
		response.setContentType(cacheEntry.getContentType());
		if (cacheEntry.getStreamContent().length > 0) {
			response.getOutputStream().write(cacheEntry.getStreamContent());
		} else {
			response.getWriter().print(cacheEntry.getWriterContent());
		}
	}

	protected String buildIdentifier(final HttpServletRequest request) {
		return request.getRequestURI();
	}

}
