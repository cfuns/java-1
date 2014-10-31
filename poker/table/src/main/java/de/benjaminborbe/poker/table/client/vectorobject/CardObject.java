package de.benjaminborbe.poker.table.client.vectorobject;

import com.google.gwt.core.client.GWT;
import org.vaadin.gwtgraphics.client.Image;

public class CardObject extends Image {

	private final String backURI;

	private String frontURI;

	private int id = 0;

	public CardObject(final int id, final int x, final int y) {
		super(x, y, 50, 75, GWT.getHostPageBaseURL() + "images/cardback.jpg");
		this.id = id;
		backURI = GWT.getHostPageBaseURL() + "images/cardback.jpg";
	}

	public void setImage(final String cardimage) {
		frontURI = GWT.getHostPageBaseURL() + "images/" + cardimage + ".jpg";
	}

	public String getImage() {
		return frontURI;
	}

	public void switchImage() {
		if (this.getHref().equals(backURI)) {
			this.setHref(frontURI);
		} else {
			this.setHref(backURI);
		}
	}

}
