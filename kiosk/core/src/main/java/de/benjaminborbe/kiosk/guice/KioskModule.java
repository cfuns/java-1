package de.benjaminborbe.kiosk.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.kiosk.api.KioskService;
import de.benjaminborbe.kiosk.booking.KioskBookingConnector;
import de.benjaminborbe.kiosk.booking.KioskBookingConnectorImpl;
import de.benjaminborbe.kiosk.config.KioskConfig;
import de.benjaminborbe.kiosk.config.KioskConfigImpl;
import de.benjaminborbe.kiosk.database.KioskDatabaseConnector;
import de.benjaminborbe.kiosk.database.KioskDatabaseConnectorImpl;
import de.benjaminborbe.kiosk.service.KioskServiceImpl;
import de.benjaminborbe.kiosk.util.KioskBookingMessageMapper;
import de.benjaminborbe.kiosk.util.KioskBookingMessageMapperImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class KioskModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(KioskConfig.class).to(KioskConfigImpl.class).in(Singleton.class);
		bind(KioskDatabaseConnector.class).to(KioskDatabaseConnectorImpl.class).in(Singleton.class);
		bind(KioskBookingConnector.class).to(KioskBookingConnectorImpl.class).in(Singleton.class);
		bind(KioskBookingMessageMapper.class).to(KioskBookingMessageMapperImpl.class).in(Singleton.class);
		bind(KioskBookingConnector.class).to(KioskBookingConnectorImpl.class).in(Singleton.class);
		bind(KioskService.class).to(KioskServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
