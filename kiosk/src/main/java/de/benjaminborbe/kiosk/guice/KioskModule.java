package de.benjaminborbe.kiosk.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.kiosk.api.KioskService;
import de.benjaminborbe.kiosk.connector.KioskBookingConnector;
import de.benjaminborbe.kiosk.connector.KioskBookingConnectorImpl;
import de.benjaminborbe.kiosk.service.KioskServiceImpl;
import de.benjaminborbe.kiosk.service.LunchBookingMessageMapper;
import de.benjaminborbe.kiosk.service.LunchBookingMessageMapperImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class KioskModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(LunchBookingMessageMapper.class).to(LunchBookingMessageMapperImpl.class).in(Singleton.class);
		bind(KioskBookingConnector.class).to(KioskBookingConnectorImpl.class).in(Singleton.class);
		bind(KioskService.class).to(KioskServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
