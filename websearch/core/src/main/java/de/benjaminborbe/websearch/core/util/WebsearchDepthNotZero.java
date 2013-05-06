package de.benjaminborbe.websearch.core.util;

import com.google.common.base.Predicate;
import de.benjaminborbe.websearch.core.dao.WebsearchPageBean;
import org.slf4j.Logger;

public class WebsearchDepthNotZero implements Predicate<WebsearchPageBean> {

	private final Logger logger;

	public WebsearchDepthNotZero(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public boolean apply(final WebsearchPageBean websearchPageBean) {
		Long depth = websearchPageBean.getDepth();
		final boolean result = depth != null && depth > 0;
		logger.trace(depth + " > 0 = " + result);
		return result;
	}
}
