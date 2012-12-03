package de.benjaminborbe.tools.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class BeanSearcher<B> {

	private final class Match implements BeanMatch<B> {

		private final B bean;

		private final int counter;

		public Match(final B bean, final int counter) {
			this.bean = bean;
			this.counter = counter;
		}

		@Override
		public B getBean() {
			return bean;
		}

		@Override
		public int getMatchCounter() {
			return counter;
		}
	}

	private final class MatchComparator implements Comparator<Match> {

		@Override
		public int compare(final Match a, final Match b) {
			if (a.getMatchCounter() > b.getMatchCounter()) {
				return -1;
			}
			if (a.getMatchCounter() < b.getMatchCounter()) {
				return 1;
			}
			return 0;
		}
	}

	private static final int SEARCH_TERM_MIN_LENGTH = 2;

	private static final int COUNTER_LIMIT = 5;

	public List<BeanMatch<B>> search(final Collection<B> beans, final int limit, final String... parts) {
		final List<Match> matches = new ArrayList<Match>();
		for (final B bean : beans) {
			final int counter = match(bean, parts);
			if (counter > 0) {
				final Match match = new Match(bean, counter);
				matches.add(match);
			}
		}
		Collections.sort(matches, new MatchComparator());
		final List<BeanMatch<B>> result = new ArrayList<BeanMatch<B>>();
		int counter = 0;
		for (final Match match : matches) {
			if (counter < limit) {
				result.add(match);
				counter++;
			}
			else {
				return result;
			}
		}
		return result;
	}

	private int match(final B bean, final String... searchTerms) {
		int counter = 0;
		for (final String searchTerm : searchTerms) {
			if (searchTerm != null && searchTerm.length() >= SEARCH_TERM_MIN_LENGTH) {
				counter += match(bean, searchTerm);
			}
		}

		final Map<String, Integer> prio = getSearchPrio();
		int totalPrio = 0;
		for (final Integer p : prio.values()) {
			totalPrio += p;
		}

		return (counter * 1000) / totalPrio;
	}

	/**
	 * fieldname -> value
	 */
	private int match(final B bean, final String searchTerm) {
		final Map<String, Integer> prio = getSearchPrio();
		final String searchTermLower = searchTerm.toLowerCase();
		int rating = 0;
		final Map<String, String> values = getSearchValues(bean);
		for (final Entry<String, String> value : values.entrySet()) {
			final String fieldname = value.getKey();
			final String content = value.getValue();
			if (content != null) {
				final String contentLower = content.toLowerCase();
				int pos = -1;
				int counter = 0;
				while (counter < COUNTER_LIMIT && (pos = contentLower.indexOf(searchTermLower, pos + 1)) != -1) {
					counter++;
					final Integer amount = prio.get(fieldname);
					if (amount != null && amount > 0) {
						rating += amount;
					}
					else {
						rating++;
					}
				}
			}
		}
		return rating;
	}

	protected abstract Map<String, String> getSearchValues(final B bean);

	/**
	 * fieldname -> prio
	 */
	protected abstract Map<String, Integer> getSearchPrio();

}
