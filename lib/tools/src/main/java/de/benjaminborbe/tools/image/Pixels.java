package de.benjaminborbe.tools.image;

public interface Pixels {

	int getWidth();

	int getHeight();

	Pixels getSubPixel(int x, int y, int width, int height);

	int getPixel(int x, int y);

	Pixels getCopy();

	void setPixel(int x, int y, int rgbValue);

}
