package de.benjaminborbe.vnc.api;

import java.util.List;

public class VncServiceAdapter implements VncService {

	private final VncService vncService;

	public VncServiceAdapter(final VncService vncService) {
		this.vncService = vncService;
	}

	@Override
	public void disconnect() throws VncServiceException {
		vncService.disconnect();
	}

	@Override
	public void connect() throws VncServiceException {
		vncService.connect();
	}

	@Override
	public VncScreenContent getScreenContent() throws VncServiceException {
		return vncService.getScreenContent();
	}

	@Override
	public void keyPress(final VncKey key) throws VncServiceException {
		vncService.keyPress(key);
	}

	@Override
	public void keyRelease(final VncKey key) throws VncServiceException {
		vncService.keyRelease(key);
	}

	@Override
	public void mouseLeftButtonPress() throws VncServiceException {
		vncService.mouseLeftButtonPress();
	}

	@Override
	public void mouseLeftButtonRelease() throws VncServiceException {
		vncService.mouseLeftButtonRelease();
	}

	@Override
	public void mouseMouse(final int x, final int y) throws VncServiceException {
		vncService.mouseMouse(x, y);
	}

	@Override
	public void mouseMouse(final VncLocation location) throws VncServiceException {
		vncService.mouseMouse(location);
	}

	@Override
	public void storeImageContent() throws VncServiceException {
		vncService.storeImageContent();
	}

	@Override
	public void disconnectForce() throws VncServiceException {
		vncService.disconnectForce();
	}

	@Override
	public void storeVncPixels(final VncPixels vncPixels, final String name) throws VncServiceException {
		vncService.storeVncPixels(vncPixels, name);
	}

	@Override
	public void keyType(final List<VncKey> keys) throws VncServiceException {
		vncService.keyType(keys);
	}

	@Override
	public void keyType(final String keys) throws VncServiceException {
		vncService.keyType(keys);
	}

	@Override
	public void mouseLeftClick() throws VncServiceException {
		vncService.mouseLeftClick();
	}

	@Override
	public void mouseLeftClickDouble() throws VncServiceException {
		vncService.mouseLeftClickDouble();
	}

	@Override
	public VncKeyParser getVncKeyParser() {
		return vncService.getVncKeyParser();
	}

	@Override
	public void storeImageContent(final String name) throws VncServiceException {
		vncService.storeImageContent(name);
	}
}
