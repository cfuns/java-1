package de.benjaminborbe.monitoring.check;

import java.net.URL;

import de.benjaminborbe.monitoring.api.Check;

public class CheckResultException extends CheckResultImpl {

	private static final long serialVersionUID = 6696421041026239381L;

	public CheckResultException(final Check check, final Throwable exception, final URL url) {
		super(check, false, exception.getClass().getSimpleName(), url);
	}

}
