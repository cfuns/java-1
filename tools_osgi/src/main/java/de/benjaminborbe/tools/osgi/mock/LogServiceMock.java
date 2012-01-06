package de.benjaminborbe.tools.osgi.mock;

import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

public class LogServiceMock implements LogService {

	@Override
	public void log(final int level, final String message) {

	}

	@Override
	public void log(final int level, final String message, final Throwable exception) {

	}

	@Override
	public void log(final ServiceReference sr, final int level, final String message) {

	}

	@Override
	public void log(final ServiceReference sr, final int level, final String message, final Throwable exception) {

	}

}
