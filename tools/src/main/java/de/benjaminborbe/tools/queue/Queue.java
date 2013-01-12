package de.benjaminborbe.tools.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;

import de.benjaminborbe.tools.util.ThreadRunner;

public class Queue<M> {

	private boolean running = true;

	private final class R implements Runnable {

		@Override
		public void run() {
			while (running) {
				try {
					queueConsumer.consume(get());
				}
				catch (final Exception e) {
					// nop
				}
			}
		}
	}

	private final BlockingQueue<M> queue;

	private final Logger logger;

	private final ThreadRunner threadRunner;

	private final QueueConsumer<M> queueConsumer;

	public Queue(final Logger logger, final ThreadRunner threadRunner, final QueueConsumer<M> queueConsumer) {
		this.logger = logger;
		this.threadRunner = threadRunner;
		this.queueConsumer = queueConsumer;
		this.queue = new LinkedBlockingQueue<M>();
	}

	public void put(final M message) {
		logger.debug("put message");
		queue.offer(message);
		threadRunner.run("queueConsumer", new R());
	}

	public M get() throws InterruptedException {
		logger.debug("get message");
		return queue.take();
	}

	public void close() {
		running = false;
	}
}
