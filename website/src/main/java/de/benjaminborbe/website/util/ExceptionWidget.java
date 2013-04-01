package de.benjaminborbe.website.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;

public class ExceptionWidget extends CompositeWidget {

	private final Throwable exception;

	public ExceptionWidget(final Throwable exception) {
		this.exception = exception;
	}

	@Override
	protected Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws Exception {
		final ListWidget widgets = new ListWidget();
		widgets.add(new H2Widget("Exception occurred: " + exception.getClass().getName()));
		widgets.add("Message: " + exception.getMessage());
		widgets.add("Stack: ");
		final StringWriter sw = new StringWriter();
		exception.printStackTrace(new PrintWriter(sw));
		widgets.add(new PreWidget(sw.toString()));
		return widgets;
	}

}
