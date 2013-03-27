package de.benjaminborbe.tools.validation.constraint;

import de.benjaminborbe.api.Identifier;

public class ValidationConstraintIdentifier<T extends Identifier<String>> implements ValidationConstraint<T> {

	@Override
	public boolean validate(final T object) {
		return object != null && object.getId() != null && !object.getId().isEmpty();
	}

	@Override
	public boolean precondition(final T object) {
		return true;
	}

}
