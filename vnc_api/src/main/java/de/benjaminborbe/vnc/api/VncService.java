package de.benjaminborbe.vnc.api;

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
}
