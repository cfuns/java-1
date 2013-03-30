package de.benjaminborbe.virt.core.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import de.benjaminborbe.virt.api.VirtService;
import de.benjaminborbe.virt.core.dao.VirtNetworkDao;
import de.benjaminborbe.virt.core.dao.VirtNetworkDaoStorage;
import de.benjaminborbe.virt.core.service.VirtCoreServiceImpl;
import org.slf4j.Logger;

public class VirtCoreModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(VirtNetworkDao.class).to(VirtNetworkDaoStorage.class).in(Singleton.class);
		bind(VirtService.class).to(VirtCoreServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);

		requestStaticInjection(VirtValidatorLinker.class);
	}
}
