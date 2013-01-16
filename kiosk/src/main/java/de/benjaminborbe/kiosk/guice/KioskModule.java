package de.benjaminborbe.kiosk.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.kiosk.api.KioskService;
import de.benjaminborbe.kiosk.connector.KioskBookingConnector;
import de.benjaminborbe.kiosk.connector.KioskBookingConnectorImpl;
import de.benjaminborbe.kiosk.service.KioskServiceImpl;
import de.benjaminborbe.kiosk.service.KioskBookingMessageMapper;
import de.benjaminborbe.kiosk.service.KioskBookingMessageMapperImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class KioskModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(KioskBookingMessageMapper.class).to(KioskBookingMessageMapperImpl.class).in(Singleton.class);
		bind(KioskBookingConnector.class).to(KioskBookingConnectorImpl.class).in(Singleton.class);
		bind(KioskService.class).to(KioskServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
