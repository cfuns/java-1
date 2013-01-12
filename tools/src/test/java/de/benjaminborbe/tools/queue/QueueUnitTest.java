package de.benjaminborbe.tools.queue;

import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.tools.util.Counter;
import de.benjaminborbe.tools.util.ThreadRunner;
import de.benjaminborbe.tools.util.ThreadRunnerImpl;

public class QueueUnitTest {

	private final class Message {

		private final Counter counter;

		public Message(final Counter counter) {
			this.counter = counter;
		}

		public void go() {
			counter.increase();
		}
	}

	@Test
	public void testQueue() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final Counter counter = new Counter();
		final ThreadRunner threadRunner = new ThreadRunnerImpl();
		final Queue<Message> queue = new Queue<Message>(logger, threadRunner, new QueueConsumer<Message>() {

			@Override
			public void consume(final Message message) {
				message.go();
			}
		});
		assertEquals(0, counter.get());
		queue.put(new Message(counter));
		queue.put(new Message(counter));
		Thread.sleep(100);
		assertEquals(2, counter.get());
	}
}
