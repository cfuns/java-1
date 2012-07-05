package de.benjaminborbe.confluence.service;

import java.util.List;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.confluence.api.ConfluenceService;
import de.benjaminborbe.confluence.api.ConfluenceServiceException;

@Singleton
public class ConfluenceServiceImpl implements ConfluenceService {

	private final Logger logger;

	@Inject
	public ConfluenceServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public List<String> getSpaceNames(final String confluenceUrl, final String username, final String password) throws ConfluenceServiceException {
		return null;
	}

}
