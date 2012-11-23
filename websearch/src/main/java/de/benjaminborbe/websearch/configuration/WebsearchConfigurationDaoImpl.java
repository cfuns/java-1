package de.benjaminborbe.websearch.configuration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.DaoCacheAutoIncrement;
import de.benjaminborbe.websearch.page.WebsearchPageDao;

@Singleton
public class WebsearchConfigurationDaoImpl extends DaoCacheAutoIncrement<WebsearchConfigurationBean, WebsearchConfigurationIdentifier> implements WebsearchConfigurationDao {

	private final WebsearchPageDao pageDao;

	@Inject
	public WebsearchConfigurationDaoImpl(
			final Logger logger,
			final WebsearchConfigurationIdGenerator idGenerator,
			final Provider<WebsearchConfigurationBean> provider,
			final WebsearchPageDao pageDao) {
		super(logger, idGenerator, provider);
		this.pageDao = pageDao;
		init();
	}

	protected void init() {
		try {
			logger.info("init websearch configuration");
			addUrl("http://confluence.benjamin-borbe.de", "bborbe");
			addUrl("http://www.hascode.com", "bborbe");
		}
		catch (final Exception e) {
			logger.error(e.getClass().getSimpleName(), e);
		}
	}

	protected void addUrl(final String urlStirng, final String username) throws MalformedURLException, StorageException {
		final URL url = new URL(urlStirng);
		pageDao.findOrCreate(url);

		final WebsearchConfigurationBean configuration = create();
		configuration.setUrl(url);
		configuration.setOwnerUsername(username);
		final List<String> excludes = new ArrayList<String>();
		excludes.add("?");
		configuration.setExcludes(excludes);
		save(configuration);
	}
}
