package de.benjaminborbe.vnc.api;

import java.util.List;

public interface VncService {

	void disconnect() throws VncServiceException;

	void connect() throws VncServiceException;

	VncScreenContent getScreenContent() throws VncServiceException;

	void keyPress(VncKey key) throws VncServiceException;

	void keyRelease(VncKey key) throws VncServiceException;

	void mouseLeftButtonPress() throws VncServiceException;

	void mouseLeftButtonRelease() throws VncServiceException;

	void mouseMouse(int x, int y) throws VncServiceException;

	void mouseMouse(VncLocation location) throws VncServiceException;

	void storeImageContent() throws VncServiceException;

	void storeVncPixels(VncPixels vncPixels, String name) throws VncServiceException;

	void disconnectForce() throws VncServiceException;

	List<VncLocation> diff(final VncPixels pixelsA, final VncPixels pixelsB, final int mask) throws VncServiceException;

	List<VncLocation> diff(final VncPixels pixelsA, final VncPixels pixelsB) throws VncServiceException;
}
