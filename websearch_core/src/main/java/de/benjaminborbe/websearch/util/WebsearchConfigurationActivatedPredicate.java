package de.benjaminborbe.websearch.util;

import com.google.common.base.Predicate;

import de.benjaminborbe.websearch.dao.WebsearchConfigurationBean;

public class WebsearchConfigurationActivatedPredicate implements Predicate<WebsearchConfigurationBean> {

	@Override
	public boolean apply(final WebsearchConfigurationBean confluenceInstanceBean) {
		return Boolean.TRUE.equals(confluenceInstanceBean.getActivated());
	}
}
