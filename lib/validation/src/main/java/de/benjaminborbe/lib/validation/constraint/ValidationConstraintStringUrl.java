package de.benjaminborbe.lib.validation.constraint;

import java.net.MalformedURLException;
import java.net.URL;

public class ValidationConstraintStringUrl implements ValidationConstraint<String> {

	@Override
	public boolean validate(final String object) {
		return isUrl(object);
	}

	@Override
	public boolean precondition(final String object) {
		return object != null;
	}

	public boolean isUrl(final String url) {
		try {
			new URL(url);
			return true;
		} catch (final MalformedURLException e) {
			return false;
		}
	}

}
