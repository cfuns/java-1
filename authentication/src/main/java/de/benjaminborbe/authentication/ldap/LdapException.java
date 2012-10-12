package de.benjaminborbe.authentication.ldap;

public class LdapException extends Exception {

	private static final long serialVersionUID = 6357504667105955138L;

	public LdapException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public LdapException(final String arg0) {
		super(arg0);
	}

	public LdapException(final Throwable arg0) {
		super(arg0);
	}

}
