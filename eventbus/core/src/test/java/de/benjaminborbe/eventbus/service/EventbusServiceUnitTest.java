package de.benjaminborbe.eventbus.service;

import de.benjaminborbe.eventbus.api.Event;
import de.benjaminborbe.eventbus.api.EventHandler;
import de.benjaminborbe.eventbus.api.EventbusService;
import de.benjaminborbe.eventbus.api.HandlerRegistration;
import de.benjaminborbe.lib.test.mock.EasyMockHelper;
import org.junit.Test;
import org.slf4j.Logger;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class EventbusServiceUnitTest {

	private final EasyMockHelper easyMockHelper = new EasyMockHelper();

	@Test
	public void test_addHandler__add_and_remove_has_correct_counter() throws Exception {
		final Logger logger = easyMockHelper.createNiceMock(Logger.class);
		easyMockHelper.replay();

		final EventbusService eventbusService = new EventbusServiceImpl(logger);
		final Event.Type<EventHandler> type = new Event.Type<EventHandler>();
		assertThat(eventbusService.getHandlerCount(type), is(0));
		final HandlerRegistration handlerRegistration = eventbusService.addHandler(type, new EventHandler() {

		});
		assertThat(eventbusService.getHandlerCount(type), is(1));
		handlerRegistration.removeHandler();
		assertThat(eventbusService.getHandlerCount(type), is(0));
		easyMockHelper.verify();
	}
}
