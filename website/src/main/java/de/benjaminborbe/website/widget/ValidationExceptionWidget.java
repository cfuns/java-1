package de.benjaminborbe.website.widget;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.util.CompositeWidget;
import de.benjaminborbe.website.util.UlWidget;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ValidationExceptionWidget extends CompositeWidget {

	private final ValidationException validationException;

	public ValidationExceptionWidget(final ValidationException validationException) {
		this.validationException = validationException;
	}

	@Override
	protected Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws Exception {
		final UlWidget ul = new UlWidget();
		for (final ValidationError validationError : validationException.getErrors()) {
			ul.add(validationError.getMessage());
		}
		return ul;
	}

}
