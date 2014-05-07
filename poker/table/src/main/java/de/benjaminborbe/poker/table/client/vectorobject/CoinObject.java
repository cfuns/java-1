package de.benjaminborbe.poker.table.client.vectorobject;

import org.vaadin.gwtgraphics.client.shape.Circle;

public class CoinObject extends Circle {

	private static String colorString = "yellow";

	private int coinValue = 0, xValue = 0, yValue = 0;

	public CoinObject(int coinValue, int x, int y) {
		super(x, y, 5);
		this.coinValue = coinValue;
		this.setFillColor(colorString);
	}

	public int getxValue() {
		return xValue;
	}

	public void setxValue(int xValue) {
		this.xValue = xValue;
	}

	public int getyValue() {
		return yValue;
	}

	public void setyValue(int yValue) {
		this.yValue = yValue;
	}

	public Integer getCoinValue() {
		return coinValue;
	}

	public void setCoinValue(Integer coinValue) {
		this.coinValue = coinValue;
	}

}
