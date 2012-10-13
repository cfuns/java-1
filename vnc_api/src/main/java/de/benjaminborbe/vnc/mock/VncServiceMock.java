package de.benjaminborbe.vnc.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.vnc.api.VncService;

@Singleton
public class VncServiceMock implements VncService {

	@Inject
	public VncServiceMock() {
	}

	@Override
	public void execute() {
	}
}
