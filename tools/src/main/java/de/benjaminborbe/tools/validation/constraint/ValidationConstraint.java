package de.benjaminborbe.tools.validation.constraint;

public interface ValidationConstraint<T> {

	boolean precondition(T object);

	boolean validate(T object);
}
