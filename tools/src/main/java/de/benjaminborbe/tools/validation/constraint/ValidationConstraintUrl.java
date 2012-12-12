package de.benjaminborbe.tools.validation.constraint;

import de.benjaminborbe.tools.url.UrlUtil;

public class ValidationConstraintUrl implements ValidationConstraint<String> {

	private final UrlUtil urlUtil;

	public ValidationConstraintUrl(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	@Override
	public boolean validate(final String object) {
		return object != null && urlUtil.isUrl(object);
	}

}
