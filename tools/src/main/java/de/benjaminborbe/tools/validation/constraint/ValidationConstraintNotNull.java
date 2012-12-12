package de.benjaminborbe.tools.validation.constraint;

public class ValidationConstraintNotNull<T> implements ValidationConstraint<T> {

	@Override
	public boolean validate(final T object) {
		return object != null;
	}

}
