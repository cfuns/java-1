package de.benjaminborbe.tools.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecuterImpl implements ThreadPoolExecuter {

	// This is the one who manages and start the work
	private final ThreadPoolExecutor threadPool;

	// Working queue for jobs (Runnable). We add them finally here
	private final ArrayBlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(5);

	public ThreadPoolExecuterImpl(final String threadName, final int corePoolSize, final int maxPoolSize, final long keepAliveTime) {
		threadPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
		threadPool.setThreadFactory(new ThreadFactory() {

			@Override
			public Thread newThread(final Runnable runnable) {
				return new Thread(runnable, threadName);
			}
		});
	}

	@Override
	public void execute(final Runnable command) {
		threadPool.execute(command);
	}

	@Override
	public void shutDown() {
		threadPool.shutdown();
	}
}
