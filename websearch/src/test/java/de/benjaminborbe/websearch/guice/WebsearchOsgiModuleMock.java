package de.benjaminborbe.websearch.guice;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.crawler.api.CrawlerService;
import de.benjaminborbe.crawler.mock.CrawlerServiceMock;
import de.benjaminborbe.index.api.IndexSearcherService;
import de.benjaminborbe.index.api.IndexSearcherServiceMock;
import de.benjaminborbe.index.api.IndexerService;
import de.benjaminborbe.index.api.IndexerServiceMock;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.mock.StorageServiceMock;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.osgi.mock.LogServiceMock;

public class WebsearchOsgiModuleMock extends AbstractModule {

	@Override
	protected void configure() {
		bind(StorageService.class).to(StorageServiceMock.class).in(Singleton.class);
		bind(CrawlerService.class).to(CrawlerServiceMock.class).in(Singleton.class);
		bind(IndexSearcherService.class).to(IndexSearcherServiceMock.class).in(Singleton.class);
		bind(IndexerService.class).to(IndexerServiceMock.class).in(Singleton.class);
		bind(LogService.class).to(LogServiceMock.class).in(Singleton.class);
		bind(ExtHttpService.class).to(ExtHttpServiceMock.class).in(Singleton.class);
	}
}
