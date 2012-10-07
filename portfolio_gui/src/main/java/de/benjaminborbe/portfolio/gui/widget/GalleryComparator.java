package de.benjaminborbe.portfolio.gui.widget;

import java.util.Comparator;

import de.benjaminborbe.gallery.api.Gallery;

public class GalleryComparator implements Comparator<Gallery> {

	@Override
	public int compare(final Gallery arg0, final Gallery arg1) {
		return arg0.getName().compareTo(arg1.getName());
	}
}
