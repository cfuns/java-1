package de.benjaminborbe.website.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.http.HttpServletResponseBuffer;
import de.benjaminborbe.tools.util.ThreadResult;

public class WidgetThreadRunnable implements Runnable {

	private final HttpServletRequest request;

	private final HttpServletResponse response;

	private final Widget widget;

	private final ThreadResult<String> threadResult = new ThreadResult<String>();

	private final HttpContext context;

	public WidgetThreadRunnable(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context, final Widget widget) {
		this.request = request;
		this.response = response;
		this.context = context;
		this.widget = widget;
	}

	@Override
	public void run() {
		try {
			final HttpServletResponseBuffer httpServletResponseAdapter = new HttpServletResponseBuffer(response);
			widget.render(request, response, context);
			threadResult.set(httpServletResponseAdapter.getStringWriter().toString());
		}
		catch (final IOException e) {
		}
	}

	public String getResult() {
		return threadResult.get();
	}
}
