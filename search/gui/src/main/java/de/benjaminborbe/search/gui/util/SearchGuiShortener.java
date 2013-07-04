package de.benjaminborbe.search.gui.util;

import de.benjaminborbe.search.gui.SearchGuiConstants;
import de.benjaminborbe.tools.util.StringUtil;

import javax.inject.Inject;
import java.util.List;

public class SearchGuiShortener {

	private final StringUtil stringUtil;

	@Inject
	public SearchGuiShortener(final StringUtil stringUtil) {
		this.stringUtil = stringUtil;
	}

	public String shortenDescription(final String content, final List<String> words) {
		if (content.length() > SearchGuiConstants.CONTENT_MAX_CHARS) {
			Integer firstMatch = null;
			for (final String word : words) {
				final int pos = content.indexOf(word);
				if (firstMatch == null || pos != -1 && firstMatch > pos) {
					firstMatch = pos;
				}
			}
			if (firstMatch != null && firstMatch > 0) {
				return "..." + stringUtil.shortenDots(content.substring(Math.max(0, firstMatch - 20)), SearchGuiConstants.CONTENT_MAX_CHARS);
			}
		}
		return stringUtil.shortenDots(content, SearchGuiConstants.CONTENT_MAX_CHARS);
	}

	public String shortenUrl(final String urlString) {
		return stringUtil.shortenDots(urlString, SearchGuiConstants.URL_MAX_CHARS);
	}

	public String shortenTitle(final String title) {
		return stringUtil.shortenDots(title, SearchGuiConstants.TITLE_MAX_CHARS);
	}
}
