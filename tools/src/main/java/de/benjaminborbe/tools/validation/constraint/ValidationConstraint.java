package de.benjaminborbe.tools.validation.constraint;

public interface ValidationConstraint<T> {

	boolean validate(T object);
}
