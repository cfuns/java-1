package de.benjaminborbe.vnc.api;

public interface VncPixels {

	VncPixels getSubPixel(int xstart, int ystart, int xend, int yend);

	int getPixel(int x, int y);

	int getWidth();

	int getHeight();

}
