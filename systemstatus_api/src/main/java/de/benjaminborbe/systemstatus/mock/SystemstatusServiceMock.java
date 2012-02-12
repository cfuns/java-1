package de.benjaminborbe.systemstatus.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.systemstatus.api.SystemstatusService;

@Singleton
public class SystemstatusServiceMock implements SystemstatusService {

	@Inject
	public SystemstatusServiceMock() {
	}

	@Override
	public void execute() {
	}
}
