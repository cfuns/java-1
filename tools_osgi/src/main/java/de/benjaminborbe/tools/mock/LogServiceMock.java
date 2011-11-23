package de.benjaminborbe.tools.mock;

import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

public class LogServiceMock implements LogService {

	@Override
	public void log(final int level, final String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void log(final int level, final String message, final Throwable exception) {
		// TODO Auto-generated method stub

	}

	@Override
	public void log(final ServiceReference sr, final int level, final String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void log(final ServiceReference sr, final int level, final String message, final Throwable exception) {
		// TODO Auto-generated method stub

	}

}
