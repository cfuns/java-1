package de.benjaminborbe.poker.table.client.vectorobject;

import com.google.gwt.core.client.GWT;
import org.vaadin.gwtgraphics.client.Image;

public class PokerTable extends Image {

	private static final String tableURI = GWT.getHostPageBaseURL() + "images/table.jpg";

	public PokerTable(final int width, final int height) {
		super(0, 0, width, height, tableURI);
	}

}
