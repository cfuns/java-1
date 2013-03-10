package de.benjaminborbe.xmlrpc.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.xmlrpc.api.XmlrpcService;
import de.benjaminborbe.xmlrpc.service.XmlrpcServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class XmlrpcModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(XmlrpcService.class).to(XmlrpcServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
