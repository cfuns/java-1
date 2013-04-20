package de.benjaminborbe.bookmark.gui.util;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookmarkGuiKeywordUtil {

	@Inject
	public BookmarkGuiKeywordUtil() {
		super();
	}

	public List<String> buildKeywords(final String keywords) {
		final List<String> result = new ArrayList<>();
		if (keywords != null) {
			final String[] parts = keywords.toLowerCase().split("\\s+");
			for (final String keyword : parts) {
				if (keyword != null && keyword.length() > 0) {
					result.add(keyword);
				}
			}
		}

		Collections.sort(result);

		return result;
	}
}
