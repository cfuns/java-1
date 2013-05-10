package de.benjaminborbe.lib.validation.constraint;

public interface ValidationConstraint<T> {

	boolean precondition(T object);

	boolean validate(T object);
}
