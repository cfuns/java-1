package de.benjaminborbe.vnc.api;

public interface VncService {

	void disconnect();

	void connect();

	VncScreenContent getScreenContent();

	void keyPress(VncKey key);

	void keyRelease(VncKey key);

	void mouseLeftButtonPress();

	void mouseLeftButtonRelease();

	void mouseMouse(int x, int y);
}
