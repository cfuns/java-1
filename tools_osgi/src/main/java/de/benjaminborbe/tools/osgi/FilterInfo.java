package de.benjaminborbe.tools.osgi;

import java.util.Dictionary;

import javax.servlet.Filter;

import org.osgi.service.http.HttpContext;

public class FilterInfo {

	private final Filter filter;

	private final String pattern;

	private final int ranking;

	private final boolean slashFilter;

	public FilterInfo(final Filter filter, final String pattern, final int ranking) {
		this(filter, pattern, ranking, false);
	}

	public FilterInfo(final Filter filter, final String pattern, final int ranking, final boolean slashFilter) {
		this.filter = filter;
		this.pattern = pattern;
		this.ranking = ranking;
		this.slashFilter = slashFilter;
	}

	public int getRanking() {
		return ranking;
	}

	public String getPattern() {
		return pattern;
	}

	public Filter getFilter() {
		return filter;
	}

	@SuppressWarnings("rawtypes")
	public Dictionary getInitParams() {
		return null;
	}

	public HttpContext getContext() {
		return null;
	}

	public boolean isSlashFilter() {
		return slashFilter;
	}
}
