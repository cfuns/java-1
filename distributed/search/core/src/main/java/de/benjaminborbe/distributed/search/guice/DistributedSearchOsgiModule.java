package de.benjaminborbe.distributed.search.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.distributed.index.api.DistributedIndexService;
import de.benjaminborbe.storage.api.StorageService;
import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import static org.ops4j.peaberry.Peaberry.service;

public class DistributedSearchOsgiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(DistributedIndexService.class).toProvider(service(DistributedIndexService.class).single());
		bind(StorageService.class).toProvider(service(StorageService.class).single());
		bind(LogService.class).toProvider(service(LogService.class).single());
		bind(ExtHttpService.class).toProvider(service(ExtHttpService.class).single());
	}
}
