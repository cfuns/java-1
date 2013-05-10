package de.benjaminborbe.lib.validation.constraint;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class ValidationConstraintStringEmail implements ValidationConstraint<String> {

	public ValidationConstraintStringEmail() {
	}

	@Override
	public boolean validate(final String object) {
		return isValidEmailAddress(object);
	}

	@Override
	public boolean precondition(final String object) {
		return object != null;
	}

	public static boolean isValidEmailAddress(String email) {
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex) {
			result = false;
		}
		return result;
	}
}
