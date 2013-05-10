package de.benjaminborbe.lib.validation.constraint;

import de.benjaminborbe.tools.url.UrlUtil;

public class ValidationConstraintStringUrl implements ValidationConstraint<String> {

	private final UrlUtil urlUtil;

	public ValidationConstraintStringUrl(final UrlUtil urlUtil) {
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
