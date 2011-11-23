package de.benjaminborbe.index.util;

import java.util.Date;

public class Duration {

	private final long startTime;

	public Duration() {
		startTime = getNowTime();
	}

	protected long getNowTime() {
		return new Date().getTime();
	}

	public long getTime() {
		return getNowTime() - startTime;
	}
}
