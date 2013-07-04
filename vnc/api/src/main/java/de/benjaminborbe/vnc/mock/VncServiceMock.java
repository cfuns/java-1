package de.benjaminborbe.vnc.mock;

import de.benjaminborbe.vnc.api.VncKey;
import de.benjaminborbe.vnc.api.VncKeyParser;
import de.benjaminborbe.vnc.api.VncLocation;
import de.benjaminborbe.vnc.api.VncPixels;
import de.benjaminborbe.vnc.api.VncScreenContent;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.api.VncServiceException;

import java.util.List;

public class VncServiceMock implements VncService {

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
	public void mouseMouse(final VncLocation vncLocation) throws VncServiceException {
	}

	@Override
	public void storeImageContent() throws VncServiceException {
	}

	@Override
	public void disconnectForce() throws VncServiceException {
	}

	@Override
	public void storeVncPixels(final VncPixels vncPixels, final String name) throws VncServiceException {
	}

	@Override
	public void keyType(final List<VncKey> keys) throws VncServiceException {
	}

	@Override
	public void keyType(final String keys) throws VncServiceException {
	}

	@Override
	public void mouseLeftClick() throws VncServiceException {
	}

	@Override
	public void mouseLeftClickDouble() throws VncServiceException {
	}

	@Override
	public VncKeyParser getVncKeyParser() {
		return null;
	}

	@Override
	public void storeImageContent(final String name) throws VncServiceException {
	}

	@Override
	public void keyType(final VncKey key) throws VncServiceException {
	}
}
