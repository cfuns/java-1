package de.benjaminborbe.tools.validation.constraint;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class ValidationConstraintStringEmail implements ValidationConstraint<String> {

	@Override
	public boolean validate(final String object) {
		try {
			InternetAddress.parse(object);
			return object.indexOf('@') != -1;
		} catch (final AddressException e) {
			return false;
		}
	}

	@Override
	public boolean precondition(final String object) {
		return object != null;
	}

}
