package de.benjaminborbe.tools.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;

import de.benjaminborbe.tools.util.ThreadRunner;

public class Queue<M> {

	private boolean running = false;

	private final class AddMessage implements Runnable {

		private final M message;

		public AddMessage(final M message) {
			this.message = message;
		}

		@Override
		public void run() {
			logger.trace("put message");
			queue.offer(message);
			if (!running) {
				threadRunner.run("queueConsumer", new ConsumeMessage());
				running = true;
			}
		}

	}

	private final class ConsumeMessage implements Runnable {

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
		threadRunner.run("add message", new AddMessage(message));
	}

	public M get() throws InterruptedException {
		logger.trace("get message");
		return queue.take();
	}

	public void close() {
		running = false;
	}
}
