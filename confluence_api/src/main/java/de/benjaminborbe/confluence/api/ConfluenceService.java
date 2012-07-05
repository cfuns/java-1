package de.benjaminborbe.confluence.api;

import java.util.List;

public interface ConfluenceService {

	List<String> getSpaceNames(String confluenceUrl, String username, String password) throws ConfluenceServiceException;
}
