package de.benjaminborbe.eventbus.api;

public abstract class Event<H extends EventHandler> {

	public static class Type<H> {

	}

	private Object source;

	protected Event() {
	}

	public Object getSource() {
		return source;
	}

	void setSource(final Object source) {
		this.source = source;
	}

	public abstract Type<H> getAssociatedType();

	public abstract void dispatch(H handler) throws Exception;
}
