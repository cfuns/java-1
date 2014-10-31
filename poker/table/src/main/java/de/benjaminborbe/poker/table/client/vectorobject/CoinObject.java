package de.benjaminborbe.poker.table.client.vectorobject;

import org.vaadin.gwtgraphics.client.shape.Circle;

public class CoinObject extends Circle {

	private static final String colorString = "yellow";

	private int coinValue = 0, xValue = 0, yValue = 0;

	public CoinObject(final int coinValue, final int x, final int y) {
		super(x, y, 5);
		this.coinValue = coinValue;
		this.setFillColor(colorString);
	}

	public int getxValue() {
		return xValue;
	}

	public void setxValue(final int xValue) {
		this.xValue = xValue;
	}

	public int getyValue() {
		return yValue;
	}

	public void setyValue(final int yValue) {
		this.yValue = yValue;
	}

	public Integer getCoinValue() {
		return coinValue;
	}

	public void setCoinValue(final Integer coinValue) {
		this.coinValue = coinValue;
	}

}
