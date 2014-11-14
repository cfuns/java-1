package de.benjaminborbe.eventbus.service;

import de.benjaminborbe.eventbus.api.Event;
import de.benjaminborbe.eventbus.api.EventHandler;
import de.benjaminborbe.eventbus.api.EventbusInitializedEvent;
import de.benjaminborbe.eventbus.api.EventbusInitializedEventHandler;
import de.benjaminborbe.eventbus.api.EventbusService;
import de.benjaminborbe.lib.test.mock.EasyMockHelper;
import org.junit.Test;
import org.slf4j.Logger;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EventbusServiceUnitTest {

	private final EasyMockHelper easyMockHelper = new EasyMockHelper();

	@Test
	public void test_addHandler__add_and_remove_has_correct_counter() throws Exception {
		final Logger logger = easyMockHelper.createNiceMock(Logger.class);
		easyMockHelper.replay();

		final EventbusService eventbusService = new EventbusServiceImpl(logger);
		final MyEventbusInitializedEventHandler eventbusInitializedEventHandler = new MyEventbusInitializedEventHandler();

		eventbusService.fireEvent(new EventbusInitializedEvent());
		assertThat(eventbusInitializedEventHandler.counter, is(0));

		eventbusService.addHandler(EventbusInitializedEvent.TYPE, eventbusInitializedEventHandler);
		assertThat(eventbusInitializedEventHandler.counter, is(0));

		eventbusService.fireEvent(new EventbusInitializedEvent());
		assertThat(eventbusInitializedEventHandler.counter, is(1));

		eventbusService.fireEvent(new EventbusInitializedEvent());
		assertThat(eventbusInitializedEventHandler.counter, is(2));

		eventbusService.removeHandler(EventbusInitializedEvent.TYPE, eventbusInitializedEventHandler);
		eventbusService.fireEvent(new EventbusInitializedEvent());
		assertThat(eventbusInitializedEventHandler.counter, is(2));

		easyMockHelper.verify();
	}

	private static class MyEventbusInitializedEventHandler implements EventbusInitializedEventHandler {

		private int counter;

		@Override
		public void onInitialize(final EventbusInitializedEvent event) {
			counter++;
		}

		@Override
		public Event.Type<? extends EventHandler> getType() {
			return EventbusInitializedEvent.TYPE;
		}
	}
}
