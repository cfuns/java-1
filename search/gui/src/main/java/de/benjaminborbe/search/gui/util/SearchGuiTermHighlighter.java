package de.benjaminborbe.search.gui.util;

import de.benjaminborbe.tools.util.ComparatorLength;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SearchGuiTermHighlighter {

	private final Logger logger;

	@Inject
	public SearchGuiTermHighlighter(final Logger logger) {
		this.logger = logger;
	}

	public String highlightSearchTerms(final String content, final String[] words) {
		return highlightSearchTerms(content, Arrays.asList(words));
	}

	public String highlightSearchTerms(final String content, final List<String> words) {
		if (content == null) {
			return content;
		}
		String result = content;

		Collections.sort(words, new ComparatorLength());

		for (final String word : words) {
			result = highlightSearchTerms(result, word);
		}
		return result;
	}

	public String highlightSearchTerms(final String content, final String word) {
		if (content == null || word == null || word.length() == 0) {
			return content;
		}
		logger.debug("highlight word: " + word + " in content " + content);

		final StringWriter result = new StringWriter();
		final String lowerContent = content.toLowerCase();
		final String wordLower = word.toLowerCase();
		int lastPos = 0;
		int pos = lowerContent.indexOf(wordLower, lastPos);
		while (pos != -1) {
			result.append(content.substring(lastPos, pos));
			result.append("<b>");
			lastPos = pos + word.length();
			result.append(content.substring(pos, lastPos));
			result.append("</b>");
			pos = lowerContent.indexOf(wordLower, lastPos);
		}
		result.append(content.substring(lastPos));
		return result.toString();
	}
}
