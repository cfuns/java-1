package de.benjaminborbe.tools.util;

public class ThreadResult<O extends Object> {

	private O value;

	public ThreadResult() {
	}

	public ThreadResult(final O value) {
		this.value = value;
	}

	public void set(final O value) {
		this.value = value;
	}

	public O get() {
		return value;
	}
}
