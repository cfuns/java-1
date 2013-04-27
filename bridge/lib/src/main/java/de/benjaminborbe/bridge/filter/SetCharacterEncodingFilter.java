package de.benjaminborbe.bridge.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class SetCharacterEncodingFilter implements Filter {

	public SetCharacterEncodingFilter() {
	}

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
		try {
			if (request.getCharacterEncoding() == null) {
				request.setCharacterEncoding("UTF-8");
			}
		} finally {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
	}

}
