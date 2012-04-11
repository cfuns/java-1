package de.benjaminborbe.messageservice.sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;

import com.google.inject.Injector;

import de.benjaminborbe.messageservice.guice.MessageserviceModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.util.Duration;
import de.benjaminborbe.tools.util.DurationUtil;

public class SimpleTest {

	@Test
	public void testSimple() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MessageserviceModulesMock());
		final Logger logger = injector.getInstance(Logger.class);
		final DurationUtil durationUtil = injector.getInstance(DurationUtil.class);

		// messageCount,messageSize
		final List<List<Integer>> list = new ArrayList<List<Integer>>();
		list.add(Arrays.asList(1, 1));
		list.add(Arrays.asList(10, 1));
		list.add(Arrays.asList(100, 1));
		list.add(Arrays.asList(1000, 1));

		list.add(Arrays.asList(1, 1000));
		list.add(Arrays.asList(10, 1000));
		list.add(Arrays.asList(100, 1000));
		list.add(Arrays.asList(1000, 1000));

		list.add(Arrays.asList(1, 1000 * 1000));
		list.add(Arrays.asList(10, 1000 * 1000));
		list.add(Arrays.asList(100, 1000 * 1000));
		list.add(Arrays.asList(1000, 1000 * 1000));

		// final int messageCount = 10;
		// final int messageSize = 1024;

		// produce
		for (final List<Integer> l : list) {
			final int messageCount = l.get(0);
			final int messageSize = l.get(1);
			final Duration insertDuration = durationUtil.getDuration();
			final SimpleProducer simpleProducer = injector.getInstance(SimpleProducer.class);
			simpleProducer.addMessage(messageCount, messageSize);
			logger.warn("messageCount: " + messageCount + " messageSize: " + messageSize + " time: " + insertDuration.getTime() + "/" + (insertDuration.getTime() / messageCount) + "/"
					+ (insertDuration.getTime() / messageCount / messageSize));
		}

		// // consume
		// {
		// final SimpleConsumer simpleConsumer = injector.getInstance(SimpleConsumer.class);
		// simpleConsumer.receive();
		// }

	}

}
