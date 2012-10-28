package de.benjaminborbe.vnc.api;

import java.util.List;

public interface VncService {

	void disconnect() throws VncServiceException;

	void connect() throws VncServiceException;

	VncScreenContent getScreenContent() throws VncServiceException;

	void keyType(List<VncKey> keys) throws VncServiceException;

	void keyType(String keys) throws VncServiceException;

	void keyPress(VncKey key) throws VncServiceException;

	void keyRelease(VncKey key) throws VncServiceException;

	void mouseLeftClick() throws VncServiceException;

	void mouseLeftClickDouble() throws VncServiceException;

	void mouseLeftButtonPress() throws VncServiceException;

	void mouseLeftButtonRelease() throws VncServiceException;

	void mouseMouse(int x, int y) throws VncServiceException;

	void mouseMouse(VncLocation location) throws VncServiceException;

	void storeImageContent() throws VncServiceException;

	void storeImageContent(String name) throws VncServiceException;

	void storeVncPixels(VncPixels vncPixels, String name) throws VncServiceException;

	void disconnectForce() throws VncServiceException;

	VncKeyParser getVncKeyParser();

}
