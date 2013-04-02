package de.benjaminborbe.confluence.util;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.confluence.ConfluenceConstants;
import de.benjaminborbe.confluence.dao.ConfluenceInstanceBean;

import java.util.Arrays;
import java.util.List;

public class ConfluenceIndexUtil {

	public String indexShared() {
		return ConfluenceConstants.INDEX;
	}

	public String indexPrivate(final UserIdentifier userIdentifier) {
		return ConfluenceConstants.INDEX + "-" + userIdentifier;
	}

	public List<String> both(final UserIdentifier userIdentifier) {
		return Arrays.asList(indexShared(), indexPrivate(userIdentifier));
	}

	public String getIndex(final ConfluenceInstanceBean confluenceInstanceBean) {
		final String indexName;
		if (Boolean.TRUE.equals(confluenceInstanceBean.getShared())) {
			indexName = indexShared();
		} else {
			indexName = indexPrivate(confluenceInstanceBean.getOwner());
		}
		return indexName;
	}
}
