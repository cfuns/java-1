package de.benjaminborbe.index.dao;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.dao.DaoBase;
import de.benjaminborbe.tools.util.IdGenerator;

@Singleton
public class UrlMonitorDaoImpl extends DaoBase<UrlMonitor> implements UrlMonitorDao {

	private static final int DEFAULT_TIMEOUT = 5 * 1000;

	@Inject
	public UrlMonitorDaoImpl(final Logger logger, final IdGenerator idGenerator, final Provider<UrlMonitor> provider) {
		super(logger, idGenerator, provider);
	}

	@Override
	protected void init() {
		save(createUrlMonitor("http://wwww.benjamin-borbe.de/", "Portrait - Benjamin Borbe",
				"<span class=\"photography\">photography</span>", DEFAULT_TIMEOUT));
		save(createUrlMonitor("http://confluence.rocketnews.de/", "Dashboard - Confluence", " <span>Dashboard</span>",
				DEFAULT_TIMEOUT));
	}

	protected UrlMonitor createUrlMonitor(final String url, final String titleMatch, final String bodyMatch,
			final int timeout) {
		try {
			final UrlMonitor urlMonitor = create();
			urlMonitor.setTitleMatch(titleMatch);
			urlMonitor.setBodyMatch(bodyMatch);
			urlMonitor.setTimeout(timeout);
			urlMonitor.setUrl(new URL(url));
			return urlMonitor;
		}
		catch (final MalformedURLException e) {
			logger.error("MalformedURLException", e);
			return null;
		}
	}

}
