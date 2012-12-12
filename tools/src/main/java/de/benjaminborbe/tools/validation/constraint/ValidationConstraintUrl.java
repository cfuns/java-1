package de.benjaminborbe.tools.validation.constraint;

import de.benjaminborbe.tools.url.UrlUtil;

public class ValidationConstraintUrl implements ValidationConstraint<String> {

	private final UrlUtil urlUtil;

	public ValidationConstraintUrl(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	@Override
	public boolean validate(final String object) {
		return urlUtil.isUrl(object);
	}

	@Override
	public boolean precondition(final String object) {
		return object != null;
	}

}
