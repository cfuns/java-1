package de.benjaminborbe.tools.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ThreadPoolExecuterImpl implements ThreadPoolExecuter {

	private final class ThreadFactoryImpl implements ThreadFactory {

		private final String threadName;

		private ThreadFactoryImpl(String threadName) {
			this.threadName = threadName;
		}

		@Override
		public Thread newThread(final Runnable runnable) {
			return new Thread(runnable, threadName);
		}
	}

	private final ExecutorService threadPool;

	public ThreadPoolExecuterImpl(final String threadName, final int threadAmount) {
		threadPool = Executors.newFixedThreadPool(threadAmount, new ThreadFactoryImpl(threadName));
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
