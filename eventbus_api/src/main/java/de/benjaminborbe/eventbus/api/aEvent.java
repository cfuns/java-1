package de.benjaminborbe.eventbus.api;

public abstract class aEvent<H extends aEventHandler> {

	public static class Type<H> {
	}

	private Object source;

	protected aEvent() {
	}

	public Object getSource() {
		return source;
	}

	void setSource(final Object source) {
		this.source = source;
	}

	public abstract Type<H> getAssociatedType();

	public abstract void dispatch(H handler);
}
