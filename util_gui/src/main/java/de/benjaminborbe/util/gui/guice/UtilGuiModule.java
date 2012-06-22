package de.benjaminborbe.util.gui.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import de.benjaminborbe.util.gui.util.UtilGuiPasswordGenerator;
import de.benjaminborbe.util.gui.util.UtilGuiPasswordGeneratorImpl;
import de.benjaminborbe.util.gui.util.UtilGuiTimeConvert;
import de.benjaminborbe.util.gui.util.UtilGuiTimeConvertImpl;

public class UtilGuiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(UtilGuiTimeConvert.class).to(UtilGuiTimeConvertImpl.class).in(Singleton.class);
		bind(UtilGuiPasswordGenerator.class).to(UtilGuiPasswordGeneratorImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
