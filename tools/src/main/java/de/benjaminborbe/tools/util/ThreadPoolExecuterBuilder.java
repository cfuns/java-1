package de.benjaminborbe.tools.util;

import com.google.inject.Inject;

public class ThreadPoolExecuterBuilder {

	@Inject
	public ThreadPoolExecuterBuilder() {
	}

	public ThreadPoolExecuter build(final String threadName, final int corePoolSize, final int maxPoolSize, final long keepAliveTime) {
		return new ThreadPoolExecuterImpl(threadName, corePoolSize, maxPoolSize, keepAliveTime);
	}
}
