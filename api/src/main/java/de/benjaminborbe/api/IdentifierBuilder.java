package de.benjaminborbe.api;

public interface IdentifierBuilder<T, I extends Identifier<T>> {

	I buildIdentifier(T value) throws IdentifierBuilderException;
}
