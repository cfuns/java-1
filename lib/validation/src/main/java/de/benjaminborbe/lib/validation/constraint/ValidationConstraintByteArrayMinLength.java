package de.benjaminborbe.lib.validation.constraint;

public class ValidationConstraintByteArrayMinLength implements ValidationConstraint<byte[]> {

	private final int minLength;

	public ValidationConstraintByteArrayMinLength(final int minLength) {
		this.minLength = minLength;
	}

	@Override
	public boolean validate(final byte[] object) {
		return object.length >= minLength;
	}

	@Override
	public boolean precondition(final byte[] object) {
		return object != null;
	}

}
