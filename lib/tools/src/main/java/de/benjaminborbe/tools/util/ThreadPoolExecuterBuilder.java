package de.benjaminborbe.tools.util;

import javax.inject.Inject;

public class ThreadPoolExecuterBuilder {

	@Inject
	public ThreadPoolExecuterBuilder() {
	}

	public ThreadPoolExecuter build(final String threadName, final int threadAmount) {
		return new ThreadPoolExecuterImpl(threadName, threadAmount);
	}
}
