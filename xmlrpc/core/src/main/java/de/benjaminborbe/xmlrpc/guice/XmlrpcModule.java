package de.benjaminborbe.xmlrpc.guice;

import com.google.inject.AbstractModule;
import javax.inject.Singleton;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import de.benjaminborbe.xmlrpc.api.XmlrpcService;
import de.benjaminborbe.xmlrpc.service.XmlrpcServiceImpl;
import org.slf4j.Logger;

public class XmlrpcModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(XmlrpcService.class).to(XmlrpcServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
