package de.benjaminborbe.tools.validation.constraint;

public class ValidationConstraintByteArrayMinLength implements ValidationConstraint<byte[]> {

	private final int minLength;

	public ValidationConstraintByteArrayMinLength(final int minLength) {
		this.minLength = minLength;
	}

	@Override
	public boolean validate(final byte[] object) {
		return object != null && object.length >= minLength;
	}

}
