package de.benjaminborbe.distributed.search.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import de.benjaminborbe.distributed.index.api.DistributedIndexService;
import de.benjaminborbe.distributed.index.mock.DistributedIndexServiceMock;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.mock.StorageServiceMock;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.osgi.mock.LogServiceMock;
import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

public class DistributedSearchOsgiModuleMock extends AbstractModule {

	@Override
	protected void configure() {
		bind(DistributedIndexService.class).to(DistributedIndexServiceMock.class).in(Singleton.class);
		bind(StorageService.class).to(StorageServiceMock.class).in(Singleton.class);
		bind(LogService.class).to(LogServiceMock.class).in(Singleton.class);
		bind(ExtHttpService.class).to(ExtHttpServiceMock.class).in(Singleton.class);
	}
}
