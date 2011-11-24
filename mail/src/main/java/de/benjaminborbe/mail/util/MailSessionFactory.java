package de.benjaminborbe.mail.util;

import javax.mail.Session;
import javax.naming.NamingException;

public interface MailSessionFactory {

	Session getInstance() throws NamingException;
}
