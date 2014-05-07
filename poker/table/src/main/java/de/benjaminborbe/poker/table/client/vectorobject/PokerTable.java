package de.benjaminborbe.poker.table.client.vectorobject;

import com.google.gwt.core.client.GWT;
import org.vaadin.gwtgraphics.client.Image;

public class PokerTable extends Image {

	private static String tableURI = GWT.getHostPageBaseURL() + "images/table.jpg";

	public PokerTable(int width, int height) {
		super(0, 0, width, height, tableURI);
	}

}
