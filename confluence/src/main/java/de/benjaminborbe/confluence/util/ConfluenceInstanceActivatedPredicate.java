package de.benjaminborbe.confluence.util;

import com.google.common.base.Predicate;

import de.benjaminborbe.confluence.dao.ConfluenceInstanceBean;

public class ConfluenceInstanceActivatedPredicate implements Predicate<ConfluenceInstanceBean> {

	@Override
	public boolean apply(final ConfluenceInstanceBean confluenceInstanceBean) {
		return Boolean.TRUE.equals(confluenceInstanceBean.getActivated());
	}

}
