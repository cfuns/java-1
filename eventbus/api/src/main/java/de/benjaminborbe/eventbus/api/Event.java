package de.benjaminborbe.eventbus.api;

public abstract class Event<H extends EventHandler> {

	protected Event() {
	}

	public abstract Type<H> getAssociatedType();

	public abstract void dispatch(H handler) throws Exception;

	public static class Type<H> {

		private final String name;

		public Type(final Class<H> handlerClass) {
			this.name = handlerClass.getName();
		}

		public String toString() {
			return name;
		}
	}
}
