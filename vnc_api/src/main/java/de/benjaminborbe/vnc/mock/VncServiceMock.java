package de.benjaminborbe.vnc.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.vnc.api.VncKey;
import de.benjaminborbe.vnc.api.VncLocation;
import de.benjaminborbe.vnc.api.VncScreenContent;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.api.VncServiceException;

@Singleton
public class VncServiceMock implements VncService {

	@Inject
	public VncServiceMock() {
	}

	@Override
	public void disconnect() {
	}

	@Override
	public void connect() {
	}

	@Override
	public VncScreenContent getScreenContent() {
		return null;
	}

	@Override
	public void keyPress(final VncKey key) {
	}

	@Override
	public void mouseLeftButtonPress() {
	}

	@Override
	public void mouseLeftButtonRelease() {
	}

	@Override
	public void keyRelease(final VncKey key) {
	}

	@Override
	public void mouseMouse(final int x, final int y) {
	}

	@Override
	public void mouseMouse(final VncLocation fishingButton) throws VncServiceException {
	}
}
