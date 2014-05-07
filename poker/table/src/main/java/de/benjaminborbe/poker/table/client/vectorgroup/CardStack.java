package de.benjaminborbe.poker.table.client.vectorgroup;

import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Image;

public class CardStack extends Group {

	private int stackSize = 0;

	public CardStack(int size) {
		super();
		this.setStackSize(size);
	}

	public int getStackSize() {
		return stackSize;
	}

	public void setStackSize(int stackSize) {
		this.stackSize = stackSize;
	}

	public Image getNextCard(Group newGroup) {
		Image object = (Image) this.getVectorObject(this.getVectorObjectCount() - 1);
		this.remove(this.getVectorObject(this.getVectorObjectCount() - 1));
		newGroup.add(object);
		return object;
	}

}
