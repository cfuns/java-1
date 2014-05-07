package de.benjaminborbe.poker.table.client.vectorgroup;

import de.benjaminborbe.poker.table.client.vectorobject.CoinObject;
import org.vaadin.gwtgraphics.client.Group;

public class CoinStack extends Group {

	private int credits = 0, xValue = 0, yValue = 0;

	public CoinStack(int credits, int x, int y) {
		super();
		xValue = x;
		yValue = y;
		this.setCredits(credits);
		this.initCoinsAndAdd();
	}

	public int getCredits() {
		return credits;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}

	private void initCoinsAndAdd() {
		for (int i = 0; i < credits; i++) {
			if (i % 1000 == 0) {
				yValue = yValue - 2;
				this.add(new CoinObject(1, xValue, yValue));
			}

		}
	}

}
