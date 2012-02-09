package de.benjaminborbe.websearch.util;

import java.util.Collection;

import de.benjaminborbe.websearch.page.PageBean;

public interface UpdateDeterminer {

	Collection<PageBean> determineExpiredPages();
}
