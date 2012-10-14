package de.benjaminborbe.vnc.api;

public interface VncScreenContent {

	int getWidth();

	int getHeight();

	int getPixel(int x, int y);

	VncPixels getPixels(int xstart, int ystart, int xend, int yend);

	VncLocation getPointerLocation();

	VncPixels getPixels();

}
