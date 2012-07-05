package de.benjaminborbe.confluence.mock;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.confluence.api.ConfluenceService;
import de.benjaminborbe.confluence.api.ConfluenceServiceException;

@Singleton
public class ConfluenceServiceMock implements ConfluenceService {

	@Inject
	public ConfluenceServiceMock() {
	}

	@Override
	public List<String> getSpaceNames(final String confluenceUrl, final String username, final String password) throws ConfluenceServiceException {
		return new ArrayList<String>();
	}
}
