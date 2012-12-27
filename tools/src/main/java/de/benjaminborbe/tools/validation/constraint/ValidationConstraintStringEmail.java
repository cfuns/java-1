package de.benjaminborbe.tools.validation.constraint;

public class ValidationConstraintStringEmail implements ValidationConstraint<String> {

	@Override
	public boolean validate(final String object) {
		return object.indexOf('@') != -1;
	}

	@Override
	public boolean precondition(final String object) {
		return object != null;
	}

}
